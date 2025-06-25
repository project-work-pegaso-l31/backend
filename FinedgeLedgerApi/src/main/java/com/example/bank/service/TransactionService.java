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

/**
 * Business-logic per movimenti di conto.
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository txRepo;
    private final AccountRepository     accRepo;

    /* ---------------------------------------------------- */
    /* Controller → usano gli overload “rapidi” (data NOW)  */
    /* ---------------------------------------------------- */

    public TransactionDTO credit(UUID accountId,
                                 BigDecimal amount,
                                 String description) {

        return credit(accountId, amount, description, LocalDateTime.now());
    }

    public TransactionDTO debit(UUID accountId,
                                BigDecimal amount,
                                String description) {

        return debit(accountId, amount, description, LocalDateTime.now());
    }

    /* ---------------------------------------------------- */
    /* Overload completi (data custom) – usati dal seeder   */
    /* ---------------------------------------------------- */

    public TransactionDTO credit(UUID accountId,
                                 BigDecimal amount,
                                 String description,
                                 LocalDateTime when) {

        return move(accountId, amount, description, when, Transaction.Type.CREDIT);
    }

    public TransactionDTO debit(UUID accountId,
                                BigDecimal amount,
                                String description,
                                LocalDateTime when) {

        return move(accountId, amount, description, when, Transaction.Type.DEBIT);
    }

    /* ---------------------------------------------------- */

    private TransactionDTO move(UUID accountId,
                                BigDecimal amount,
                                String description,
                                LocalDateTime when,
                                Transaction.Type type) {

        Account acc = accRepo.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Conto non trovato"));

        BigDecimal newBalance = (type == Transaction.Type.CREDIT)
                ? acc.getBalance().add(amount)
                : acc.getBalance().subtract(amount);

        if (type == Transaction.Type.DEBIT && newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Saldo insufficiente");
        }

        /* aggiorna saldo conto */
        acc.setBalance(newBalance);

        Transaction tx = Transaction.builder()
                .accountId(accountId)
                .amount(type == Transaction.Type.DEBIT ? amount.negate() : amount)
                .type(type)
                .description(description)
                .createdAt(when)
                .build();

        txRepo.save(tx);   // persiste movimento
        accRepo.save(acc); // persiste saldo

        return toDTO(tx);
    }

    /* ---------------------------------------------------- */
    public List<TransactionDTO> listByAccount(UUID accountId) {
        return txRepo.findByAccount(accountId).stream().map(this::toDTO).toList();
    }

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
