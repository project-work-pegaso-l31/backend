package com.example.bank.repository;

import com.example.bank.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    // puoi aggiungere query methods, es.: Optional<Customer> findByEmail(String email);
}
