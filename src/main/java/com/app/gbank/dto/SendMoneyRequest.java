package com.app.gbank.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record SendMoneyRequest(
        @NotBlank(message = "Recipient mobile number is required")
        @Pattern(regexp = "^09\\d{9}$", message = "Must be a valid PH mobile number")
        String recipientMobileNumber,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "1.00", message = "Minimum send amount is ₱1.00")
        @DecimalMax(value = "50000.00", message = "Maximum single send is ₱50,000.00")
        BigDecimal amount,

        @Size(max = 200, message = "Note must not exceed 200 characters")
        String note
) {}