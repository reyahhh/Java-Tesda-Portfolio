package com.app.gbank.dto;

import com.app.gbank.enums.Role;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserSummaryDto(
        Long id,
        String fullName,
        String mobileNumber,
        String email,
        Role role,
        BigDecimal balance,
        boolean enabled,
        LocalDateTime createdAt
) {}