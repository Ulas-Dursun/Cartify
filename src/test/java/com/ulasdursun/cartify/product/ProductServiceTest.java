package com.ulasdursun.cartify.product;

import com.ulasdursun.cartify.exception.ProductNotFoundException;
import com.ulasdursun.cartify.product.dto.ProductRequest;
import com.ulasdursun.cartify.product.dto.ProductResponse;
import com.ulasdursun.cartify.product.dto.StockUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;

    @InjectMocks private ProductService productService;

    @Test
    void createProduct_withValidRequest_returnsProductResponse() {
        ProductRequest request = new ProductRequest("Laptop", "High-end laptop", new BigDecimal("1500.00"), 20);

        Product saved = Product.builder()
                .id(UUID.randomUUID())
                .name("Laptop")
                .description("High-end laptop")
                .price(new BigDecimal("1500.00"))
                .stock(20)
                .active(true)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponse response = productService.createProduct(request);

        assertThat(response.name()).isEqualTo("Laptop");
        assertThat(response.price()).isEqualByComparingTo(new BigDecimal("1500.00"));
        assertThat(response.stock()).isEqualTo(20);
        assertThat(response.active()).isTrue();
    }

    @Test
    void getAllActiveProducts_returnsOnlyActiveProducts() {
        Product active = Product.builder()
                .id(UUID.randomUUID())
                .name("Active Product")
                .price(new BigDecimal("100.00"))
                .stock(10)
                .active(true)
                .build();

        when(productRepository.findAllByActiveTrue()).thenReturn(List.of(active));

        List<ProductResponse> results = productService.getAllActiveProducts();

        assertThat(results).hasSize(1);
        assertThat(results.get(0).active()).isTrue();
    }

    @Test
    void updateStock_withValidRequest_updatesStockCorrectly() {
        UUID productId = UUID.randomUUID();

        Product product = Product.builder()
                .id(productId)
                .name("Widget")
                .price(new BigDecimal("50.00"))
                .stock(5)
                .active(true)
                .build();

        when(productRepository.findByIdAndActiveTrue(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductResponse response = productService.updateStock(productId, new StockUpdateRequest(50));

        assertThat(response.stock()).isEqualTo(50);
        verify(productRepository).save(product);
    }

    @Test
    void softDeleteProduct_setsActiveFalse() {
        UUID productId = UUID.randomUUID();

        Product product = Product.builder()
                .id(productId)
                .name("Widget")
                .price(new BigDecimal("50.00"))
                .stock(10)
                .active(true)
                .build();

        when(productRepository.findByIdAndActiveTrue(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        productService.softDeleteProduct(productId);

        assertThat(product.getActive()).isFalse();
        verify(productRepository).save(product);
    }

    @Test
    void getActiveProductById_withNonExistentId_throwsProductNotFoundException() {
        UUID missingId = UUID.randomUUID();

        when(productRepository.findByIdAndActiveTrue(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getActiveProductById(missingId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(missingId.toString());
    }
}