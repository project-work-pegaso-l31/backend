package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.domain.Customer;
import com.example.bank.dto.*;
import com.example.bank.exception.NotFoundException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepo;
    private final CustomerRepository customerRepo;

    public AccountDTO openAccount(CreateAccountDTO dto) {
        Customer c = customerRepo.findById(dto.customerId())
                .orElseThrow(() -> new NotFoundException("Customer"));

        accountRepo.findByIban(dto.iban()).ifPresent(a -> {
            throw new IllegalArgumentException("IBAN già esistente");
        });

        Account a = Account.builder()
                .iban(dto.iban())
                .balance(BigDecimal.ZERO)
                .customerId(c.getId())
                .build();

        return toDto(accountRepo.save(a));
    }

    public AccountDTO get(UUID id) {
        Account a = accountRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Account"));
        return toDto(a);
    }

    public List<AccountDTO> listByCustomer(UUID customerId) {
        return accountRepo.findByCustomer(customerId).stream().map(this::toDto).toList();
    }

    private AccountDTO toDto(Account a) {
        return new AccountDTO(a.getId(), a.getIban(), a.getBalance(), a.getCustomerId());
    }
}
