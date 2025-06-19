package com.example.bank.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Dati conto restituiti dalle API.
 */
public record AccountDTO(
        UUID id,
        String iban,
        BigDecimal balance,
        UUID customerId
) { }
