package com.app.gbank.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record LoadPurchaseRequest(
        @NotBlank(message = "Target mobile number is required")
        @Pattern(regexp = "^09\\d{9}$", message = "Must be a valid PH mobile number")
        String targetMobileNumber,

        @NotBlank(message = "Network is required")
        String network,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "5.00", message = "Minimum load is ₱5.00")
        @DecimalMax(value = "1000.00", message = "Maximum load is ₱1,000.00")
        BigDecimal amount
) {}