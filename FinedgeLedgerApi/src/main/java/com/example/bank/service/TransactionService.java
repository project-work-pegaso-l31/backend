package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.domain.Transaction;
import com.example.bank.domain.Transaction.Type;
import com.example.bank.dto.TransactionDTO;
import com.example.bank.exception.NotFoundException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository txRepo;
    private final AccountRepository     accountRepo;

    /* ───── CREDIT ───── */
    public TransactionDTO credit(UUID accountId,
                                 BigDecimal amount,
                                 String description) {

        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account non trovato"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Importo deve essere positivo");

        account.setBalance(account.getBalance().add(amount));

        Transaction tx = Transaction.builder()
                .accountId(accountId)
                .amount(amount)
                .type(Type.CREDIT)
                .description(description)
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        txRepo.save(tx);
        return toDTO(tx);
    }

    /* ───── DEBIT ───── */
    public TransactionDTO debit(UUID accountId,
                                BigDecimal amount,
                                String description) {

        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account non trovato"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Importo deve essere positivo");

        if (account.getBalance().compareTo(amount) < 0)
            throw new IllegalArgumentException("Saldo insufficiente");

        account.setBalance(account.getBalance().subtract(amount));

        Transaction tx = Transaction.builder()
                .accountId(accountId)
                .amount(amount.negate())
                .type(Type.DEBIT)
                .description(description)
                .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        txRepo.save(tx);
        return toDTO(tx);
    }

    /* ───── LIST ───── */
    public List<TransactionDTO> listByAccount(UUID accountId) {
        return txRepo.findByAccount(accountId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /* ───── MAPPING ───── */
    private TransactionDTO toDTO(Transaction t) {
        return new TransactionDTO(
                t.getId(),
                t.getAccountId(),
                t.getAmount(),
                t.getType().name(),
                t.getDescription(),
                t.getCreatedAt().toString()
        );
    }
}
