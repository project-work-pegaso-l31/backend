package com.example.bank.domain;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Account {
    private UUID id;
    private String iban;
    private BigDecimal balance;
    private UUID customerId;
}
