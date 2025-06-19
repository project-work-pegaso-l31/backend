package com.example.bank.repository;

import com.example.bank.domain.Account;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountRepository {
    private final Map<UUID, Account> store = new ConcurrentHashMap<>();

    public Account save(Account a) {
        if (a.getId() == null) a.setId(UUID.randomUUID());
        store.put(a.getId(), a);
        return a;
    }

    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<Account> findByIban(String iban) {
        return store.values().stream()
                .filter(a -> a.getIban().equals(iban))
                .findFirst();
    }

    public List<Account> findByCustomer(UUID customerId) {
        return store.values().stream()
                .filter(a -> a.getCustomerId().equals(customerId))
                .toList();
    }
}
