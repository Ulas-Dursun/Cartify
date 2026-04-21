package com.ulasdursun.cartify.auth.dto;

public record AuthResponse(
        String token,
        String email,
        String role
) {}