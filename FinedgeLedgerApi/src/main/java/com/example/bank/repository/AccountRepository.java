package com.example.bank.repository;

import com.example.bank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByIban(String iban);   // metodo custom d’esempio
}
