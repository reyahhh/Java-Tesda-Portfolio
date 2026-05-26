package com.app.gbank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AddFundsRequest(
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Minimum amount is ₱1.00")
    BigDecimal amount,

    @NotBlank(message = "Source is required")
    String source
) {}