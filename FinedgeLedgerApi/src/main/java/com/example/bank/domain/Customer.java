package com.example.bank.domain;

import lombok.*;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    private UUID id;
    private String fullName;
    private String email;
    private String fiscalCode;
}
