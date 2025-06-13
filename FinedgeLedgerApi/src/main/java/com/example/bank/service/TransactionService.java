package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.domain.Transaction;
import com.example.bank.dto.TransactionDTO;
import com.example.bank.exception.NotFoundException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository txRepo;
    private final AccountRepository accountRepo;

    public TransactionDTO credit(UUID accountId, BigDecimal amount, String description) {
        return createTx(accountId, amount, description, Transaction.Type.CREDIT);
    }

    public TransactionDTO debit(UUID accountId, BigDecimal amount, String description) {
        return createTx(accountId, amount.negate(), description, Transaction.Type.DEBIT);
    }

    public List<TransactionDTO> listByAccount(UUID accountId) {
        return txRepo.findByAccount_Id(accountId).stream().map(this::toDto).toList();
    }

    /* --------- Interni --------- */
    private TransactionDTO createTx(UUID accountId, BigDecimal amount,
                                    String description, Transaction.Type type) {

        Account acc = accountRepo.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account"));

        /* aggiorna saldo */
        acc.setBalance(acc.getBalance().add(amount));

        Transaction tx = Transaction.builder()
                .amount(amount.abs())
                .type(type)
                .description(description)
                .account(acc)
                .build();
        txRepo.save(tx);
        return toDto(tx);
    }

    private TransactionDTO toDto(Transaction t){
        return new TransactionDTO(
                t.getId(), t.getAmount(), t.getType().name(),
                t.getDescription(), t.getCreatedAt(), t.getAccount().getId());
    }
}
