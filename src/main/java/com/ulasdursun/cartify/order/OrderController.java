package com.ulasdursun.cartify.order;

import com.ulasdursun.cartify.order.dto.CreateOrderRequest;
import com.ulasdursun.cartify.order.dto.OrderResponse;
import com.ulasdursun.cartify.order.dto.UpdateOrderStatusRequest;
import com.ulasdursun.cartify.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create a new order")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return orderService.createOrder(request, authenticatedUser);
    }

    @Operation(summary = "Get order by ID — owner or ADMIN only")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse getOrderById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return orderService.getOrderById(id, authenticatedUser);
    }

    @Operation(summary = "List all orders for the authenticated user")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getMyOrders(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return orderService.getOrdersForUser(authenticatedUser);
    }

    @Operation(summary = "Update order status — ADMIN only")
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return orderService.updateOrderStatus(id, request);
    }
    @Operation(summary = "Get all orders — ADMIN only")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }
}