package com.example.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDTO(
        UUID id,
        BigDecimal amount,
        String type,
        String description,
        LocalDateTime createdAt,
        UUID accountId
) {}
