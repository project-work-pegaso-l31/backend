package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.domain.Transaction;
import com.example.bank.dto.TransactionDTO;
import com.example.bank.exception.InsufficientFundsException;
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
    private final AccountRepository accRepo;

    /* --- API normali (usa now) ------------------------------------------------ */

    public TransactionDTO credit(UUID id, BigDecimal amount, String desc) {
        return move(id, amount, desc, LocalDateTime.now());
    }

    public TransactionDTO debit(UUID id, BigDecimal amount, String desc) {
        return move(id, amount.negate(), desc, LocalDateTime.now());
    }

    /* --- Overload usato dal seeder (timestamp custom) ------------------------ */

    public TransactionDTO credit(UUID id, BigDecimal amount, String desc, LocalDateTime at) {
        return move(id, amount, desc, at);
    }

    public TransactionDTO debit(UUID id, BigDecimal amount, String desc, LocalDateTime at) {
        return move(id, amount.negate(), desc, at);
    }

    /* --- common logic -------------------------------------------------------- */

    private TransactionDTO move(UUID accId, BigDecimal delta, String desc, LocalDateTime at) {
        Account acc = accRepo.findById(accId)
                .orElseThrow(() -> new NotFoundException("Conto non trovato"));

        BigDecimal nuovoSaldo = acc.getBalance().add(delta);
        if (nuovoSaldo.signum() < 0)
            throw new InsufficientFundsException("Saldo insufficiente");

        acc.setBalance(nuovoSaldo);

        Transaction tx = Transaction.builder()
                .id(UUID.randomUUID())
                .accountId(accId)
                .amount(delta)
                .type(delta.signum() >= 0 ? Transaction.Type.CREDIT : Transaction.Type.DEBIT)
                .description(desc == null || desc.isBlank() ? null : desc)
                .createdAt(at)
                .build();

        txRepo.save(tx);
        return toDTO(tx);
    }

    public List<TransactionDTO> listByAccount(UUID accId) {
        return txRepo.findByAccount(accId).stream().map(this::toDTO).toList();
    }

    private TransactionDTO toDTO(Transaction t) {
        return new TransactionDTO(
                t.getId(), t.getAccountId(), t.getAmount(),
                t.getType().name(), t.getDescription(), t.getCreatedAt().toString());
    }
}
