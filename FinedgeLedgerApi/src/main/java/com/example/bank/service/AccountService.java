package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.dto.AccountDTO;
import com.example.bank.dto.CreateAccountDTO;
import com.example.bank.exception.NotFoundException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.util.IbanGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepo;

    /* ------------------------------------------------------------------ */

    /** Apre un nuovo conto con IBAN generato automaticamente. */
    public AccountDTO openAccount(CreateAccountDTO dto) {

        // 1) genera IBAN finché non esiste già
        String iban;
        do {
            iban = IbanGenerator.newItalian();
        } while (accountRepo.findByIban(iban).isPresent());

        // 2) crea entità
        Account account = Account.builder()
                .customerId(dto.customerId())
                .iban(iban)
                .balance(BigDecimal.ZERO)
                .build();

        accountRepo.save(account);
        return toDTO(account);
    }

    /** Ritorna un singolo conto. */
    public AccountDTO get(UUID id) {
        return accountRepo.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("Account non trovato"));
    }

    /** Elenco conti di un cliente. */
    public List<AccountDTO> listByCustomer(UUID customerId) {
        return accountRepo.findByCustomerId(customerId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /* ---------- helper di mapping ------------------------------------- */

    private AccountDTO toDTO(Account a) {
        return new AccountDTO(
                a.getId(),
                a.getIban(),
                a.getBalance(),
                a.getCustomerId()   // ⬅︎ 4° argomento
        );
    }

}
