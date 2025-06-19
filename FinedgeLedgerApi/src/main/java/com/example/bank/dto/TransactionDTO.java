package com.example.bank.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Dati movimento (accredito/debito) restituiti dalle API.
 */
public record TransactionDTO(
        UUID id,
        UUID accountId,
        BigDecimal amount,
        String type,          // "CREDIT" o "DEBIT"
        String description,
        String createdAt      // ISO-8601 in stringa (es. 2025-06-19T17:45:00)
) { }
