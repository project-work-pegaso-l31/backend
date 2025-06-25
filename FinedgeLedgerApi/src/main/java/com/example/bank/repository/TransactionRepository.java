package com.example.bank.repository;

import com.example.bank.domain.Transaction;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TransactionRepository {

    private final Map<UUID, Transaction> store = new ConcurrentHashMap<>();

    /* ---------- CRUD base ---------- */

    public Transaction save(Transaction t) {
        if (t.getId() == null) t.setId(UUID.randomUUID());
        store.put(t.getId(), t);
        return t;
    }

    public Optional<Transaction> findById(UUID id) {          // <-- AGGIUNTO
        return Optional.ofNullable(store.get(id));
    }

    public List<Transaction> findByAccount(UUID accId) {
        return store.values().stream()
                .filter(tx -> tx.getAccountId().equals(accId))
                .toList();
    }
}
