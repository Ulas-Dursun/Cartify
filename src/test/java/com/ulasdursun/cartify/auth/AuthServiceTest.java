package com.ulasdursun.cartify.auth;

import com.ulasdursun.cartify.auth.dto.AuthResponse;
import com.ulasdursun.cartify.auth.dto.LoginRequest;
import com.ulasdursun.cartify.auth.dto.RegisterRequest;
import com.ulasdursun.cartify.exception.DuplicateEmailException;
import com.ulasdursun.cartify.user.Role;
import com.ulasdursun.cartify.user.User;
import com.ulasdursun.cartify.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks private AuthService authService;

    @Test
    void register_withValidRequest_returnsTokenAndUserRole() {
        RegisterRequest request = new RegisterRequest("user@test.com", "password123");

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });
        when(jwtService.generateToken(any(User.class))).thenReturn("mock-token");

        AuthResponse response = authService.register(request);

        assertThat(response.token()).isEqualTo("mock-token");
        assertThat(response.email()).isEqualTo("user@test.com");
        assertThat(response.role()).isEqualTo(Role.USER.name());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_withDuplicateEmail_throwsDuplicateEmailException() {
        RegisterRequest request = new RegisterRequest("existing@test.com", "password123");

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("existing@test.com");

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_withValidCredentials_returnsToken() {
        LoginRequest request = new LoginRequest("user@test.com", "password123");

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("user@test.com")
                .password("hashed")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mock-token");

        AuthResponse response = authService.login(request);

        assertThat(response.token()).isEqualTo("mock-token");
        assertThat(response.email()).isEqualTo("user@test.com");
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("user@test.com", "password123")
        );
    }

    @Test
    void login_withInvalidCredentials_throwsBadCredentialsException() {
        LoginRequest request = new LoginRequest("user@test.com", "wrongpass");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);

        verify(userRepository, never()).findByEmail(anyString());
    }
}