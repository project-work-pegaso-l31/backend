package com.example.bank.dto;

import jakarta.validation.constraints.*;

/**
 * DTO di creazione cliente.
 */
public record CreateCustomerDTO(

        @NotBlank(message = "Nome obbligatorio")
        String fullName,

        @Email(message = "Email non valida")
        String email,

        // Codice fiscale italiano: 16 caratteri alfanumerici (regex semplificata)
        @Pattern(
                regexp = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "Codice fiscale non valido"
        )
        String fiscalCode
) { }
