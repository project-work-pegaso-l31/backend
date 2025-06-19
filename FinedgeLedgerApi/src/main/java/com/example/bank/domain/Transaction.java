package com.example.bank.domain;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction {
    public enum Type { CREDIT, DEBIT }

    private UUID id;
    private UUID accountId;
    private BigDecimal amount;
    private Type type;
    private String description;
    private LocalDateTime createdAt;
}
