package com.example.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CreateAccountDTO(
        @NotNull UUID customerId,
        @Pattern(
                regexp = "IT\\d{2}[A-Z0-9]{1}\\d{10}[0-9A-Z]{12}",
                message = "IBAN italiano non valido"
        )
        @NotBlank String iban
) { }
