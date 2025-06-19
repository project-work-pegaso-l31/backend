package com.example.bank.repository;

import com.example.bank.domain.Transaction;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TransactionRepository {
    private final Map<UUID, Transaction> store = new ConcurrentHashMap<>();

    public Transaction save(Transaction t) {
        if (t.getId() == null) t.setId(UUID.randomUUID());
        store.put(t.getId(), t);
        return t;
    }

    public List<Transaction> findByAccount(UUID accountId) {
        return store.values().stream()
                .filter(t -> t.getAccountId().equals(accountId))
                .toList();
    }
}
