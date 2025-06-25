package com.example.bank.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/** DTO per aggiornare l’anagrafica di un cliente */
public record UpdateCustomerDTO(
        @NotBlank(message = "Nome obbligatorio")        String fullName,
        @Email(message = "Email non valida")            String email,
        @Pattern(
                regexp = "^[A-Z]{6}[0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{3}[A-Z]$",
                message = "Codice fiscale non valido")
        String fiscalCode
) { }
