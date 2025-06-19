package com.example.bank.dto;

import java.util.UUID;

/**
 * Dati cliente restituiti dalle API.
 */
public record CustomerDTO(
        UUID id,
        String fullName,
        String email,
        String fiscalCode
) { }
