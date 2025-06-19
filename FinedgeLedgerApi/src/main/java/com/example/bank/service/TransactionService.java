package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.domain.Transaction;
import com.example.bank.dto.TransactionDTO;
import com.example.bank.exception.NotFoundException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository txRepo;
    private final AccountRepository accountRepo;

    public TransactionDTO credit(UUID accountId, BigDecimal amount, String description) {
        return append(accountId, amount.abs(), Transaction.Type.CREDIT, description);
    }

    public TransactionDTO debit(UUID accountId, BigDecimal amount, String description) {
        return append(accountId, amount.abs(), Transaction.Type.DEBIT, description);
    }

    public List<TransactionDTO> listByAccount(UUID accountId) {
        return txRepo.findByAccount(accountId).stream().map(this::toDto).toList();
    }

    /* ------------------- private helpers ------------------- */

    private TransactionDTO append(UUID accountId, BigDecimal amount,
                                  Transaction.Type type, String description) {

        Account acc = accountRepo.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account"));

        BigDecimal signed = (type == Transaction.Type.CREDIT) ? amount : amount.negate();
        acc.setBalance(acc.getBalance().add(signed));
        accountRepo.save(acc);           // persiste nuovo saldo

        Transaction tx = Transaction.builder()
                .accountId(accountId)
                .amount(amount)
                .type(type)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();

        return toDto(txRepo.save(tx));
    }

    private TransactionDTO toDto(Transaction t) {
        return new TransactionDTO(
                t.getId(), t.getAccountId(), t.getAmount(),
                t.getType().name(), t.getDescription(),
                t.getCreatedAt().toString()
        );
    }
}
