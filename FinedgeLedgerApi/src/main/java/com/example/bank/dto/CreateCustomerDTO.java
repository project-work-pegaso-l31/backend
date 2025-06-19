package com.example.bank.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Payload in ingresso per creare un nuovo cliente.
 */
public record CreateCustomerDTO(
        @NotBlank String fullName,
        @Email String email,
        @Pattern(regexp = "\\w{16}") String fiscalCode     // 16 caratteri alfanumerici
) { }
