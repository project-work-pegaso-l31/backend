package com.example.bank.repository;

import com.example.bank.domain.Customer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomerRepository {

    private final Map<UUID, Customer> store = new ConcurrentHashMap<>();

    /* ---------- base CRUD ---------- */

    public Customer save(Customer c) {
        if (c.getId() == null) c.setId(UUID.randomUUID());
        store.put(c.getId(), c);
        return c;
    }

    public Optional<Customer> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Customer> findAll() {
        return new ArrayList<>(store.values());
    }

    /* ---------- lookup per CF ---------- */

    public Optional<Customer> findByFiscalCode(String cf) {
        return store.values().stream()
                .filter(c -> c.getFiscalCode().equalsIgnoreCase(cf))
                .findFirst();
    }
}
