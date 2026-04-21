package com.ulasdursun.cartify.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp,
        Map<String, String> validationErrors
) {
    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(status, error, message, LocalDateTime.now(), null);
    }

    public static ErrorResponse ofValidation(Map<String, String> validationErrors) {
        return new ErrorResponse(
                400,
                "Validation Failed",
                "One or more fields have invalid values",
                LocalDateTime.now(),
                validationErrors
        );
    }
}