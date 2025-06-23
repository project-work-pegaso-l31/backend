package com.example.bank.service;

import com.example.bank.domain.Transaction;
import com.example.bank.domain.Account;
import com.example.bank.dto.TransactionDTO;
import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.exception.NotFoundException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository txRepo;
    private final AccountRepository accRepo;

    public TransactionService(TransactionRepository txRepo, AccountRepository accRepo) {
        this.txRepo = txRepo;
        this.accRepo = accRepo;
    }

    /* ------------------------------------------------------------ */
    public TransactionDTO credit(UUID accountId, BigDecimal amount, String description) {
        return move(accountId, amount, description, Transaction.Type.CREDIT);
    }

    public TransactionDTO debit(UUID accountId, BigDecimal amount, String description) {
        return move(accountId, amount.negate(), description, Transaction.Type.DEBIT);
    }

    /* ------------------------------------------------------------ */
    public List<TransactionDTO> listByAccount(UUID accountId) {
        return txRepo.findByAccount(accountId).stream()
                .map(TransactionService::toDTO)
                .toList();
    }

    /* ------------------------------------------------------------ */
    private TransactionDTO move(UUID id, BigDecimal delta, String desc, Transaction.Type type) {

        Account account = accRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Conto non trovato"));

        BigDecimal newBal = account.getBalance().add(delta);

        if (newBal.compareTo(BigDecimal.ZERO) < 0)
            throw new InsufficientFundsException("Saldo insufficiente");

        account.setBalance(newBal);
        accRepo.save(account);

        Transaction tx = txRepo.save(Transaction.builder()
                .accountId(id)
                .amount(delta)
                .description(desc)
                .type(type)
                .createdAt(LocalDateTime.now())
                .build());

        return toDTO(tx);
    }

    private static TransactionDTO toDTO(Transaction t) {
        return new TransactionDTO(
                t.getId(), t.getAccountId(), t.getAmount(),
                t.getType().name(), t.getDescription(), t.getCreatedAt().toString());
    }
}
