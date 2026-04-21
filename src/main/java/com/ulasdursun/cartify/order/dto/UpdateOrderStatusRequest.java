package com.ulasdursun.cartify.order.dto;

import com.ulasdursun.cartify.order.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(

        @NotNull(message = "Status is required")
        OrderStatus status
) {}