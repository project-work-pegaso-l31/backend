package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.domain.Customer;
import com.example.bank.dto.AccountDTO;
import com.example.bank.dto.CreateAccountDTO;
import com.example.bank.exception.NotFoundException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepo;
    private final CustomerRepository customerRepo;

    public AccountDTO openAccount(CreateAccountDTO dto) {
        Customer c = customerRepo.findById(dto.customerId())
                .orElseThrow(() -> new NotFoundException("Customer"));

        Account a = Account.builder()
                .iban(dto.iban())
                .customer(c)
                .build();

        accountRepo.save(a);
        return toDto(a);
    }

    public AccountDTO get(UUID id) {
        Account a = accountRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Account"));
        return toDto(a);
    }

    /* -------- Mapper interno semplice -------- */
    private AccountDTO toDto(Account a) {
        return new AccountDTO(a.getId(), a.getIban(), a.getBalance(), a.getCustomer().getId());
    }
}
