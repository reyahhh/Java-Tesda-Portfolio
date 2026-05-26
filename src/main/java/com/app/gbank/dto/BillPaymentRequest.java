package com.app.gbank.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record BillPaymentRequest(
        @NotBlank(message = "Biller name is required")
        String billerName,

        @NotBlank(message = "Account number is required")
        String accountNumber,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "1.00", message = "Minimum bill payment is ₱1.00")
        BigDecimal amount
) {}