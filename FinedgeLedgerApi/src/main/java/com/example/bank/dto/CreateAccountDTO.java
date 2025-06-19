package com.example.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Payload in ingresso per aprire un nuovo conto.
 */
public record CreateAccountDTO(
        @NotNull UUID customerId,
        @NotBlank String iban
) { }
