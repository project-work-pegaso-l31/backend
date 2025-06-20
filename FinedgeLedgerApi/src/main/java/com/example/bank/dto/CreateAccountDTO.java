package com.example.bank.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO per l’apertura di un conto.
 * L’IBAN viene generato dal server.
 */
public record CreateAccountDTO(
        @NotNull(message = "customerId obbligatorio")
        UUID customerId
) { }
