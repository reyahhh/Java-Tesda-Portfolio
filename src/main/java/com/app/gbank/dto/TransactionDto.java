package com.app.gbank.dto;

import com.app.gbank.enums.TransactionStatus;
import com.app.gbank.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(
        Long id,
        String senderName,
        String senderMobile,
        String receiverName,
        String receiverMobile,
        TransactionType type,
        TransactionStatus status,
        BigDecimal amount,
        String description,
        String referenceNumber,
        LocalDateTime createdAt
) {}