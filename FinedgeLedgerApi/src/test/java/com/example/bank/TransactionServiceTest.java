package com.example.bank;

import com.example.bank.domain.Account;
import com.example.bank.exception.NotFoundException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionServiceTest {

    private TransactionService service;
    private AccountRepository accRepo;
    private TransactionRepository txRepo;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        accRepo = new AccountRepository();
        txRepo  = new TransactionRepository();
        service = new TransactionService(txRepo, accRepo);

        accountId = accRepo.save(
                Account.builder()
                        .iban("IT60X0542811101000000123456")
                        .balance(BigDecimal.ZERO)
                        .customerId(UUID.randomUUID())
                        .build()
        ).getId();
    }

    @Test
    void credit_updates_balance() {
        service.credit(accountId, new BigDecimal("100.00"), "bonus");
        assertEquals(new BigDecimal("100.00"),
                accRepo.findById(accountId).get().getBalance());
        assertEquals(1, txRepo.findByAccount(accountId).size());
    }

    @Test
    void debit_updates_balance() {
        service.credit(accountId, new BigDecimal("100.00"), "bonus");
        service.debit(accountId,  new BigDecimal("30.00"),  "spesa");
        assertEquals(new BigDecimal("70.00"),
                accRepo.findById(accountId).get().getBalance());
        assertEquals(2, txRepo.findByAccount(accountId).size());
    }

    @Test
    void credit_on_unknown_account_throws() {
        UUID wrongId = UUID.randomUUID();
        assertThrows(NotFoundException.class,
                () -> service.credit(wrongId, BigDecimal.TEN, ""));
    }

    @Test
    void negative_amount_is_abs_value() {
        // anche se dovesse arrivare un valore negativo viene normalizzato in abs()
        service.credit(accountId, new BigDecimal("-50.00"), "correzione");
        assertEquals(new BigDecimal("50.00"),
                accRepo.findById(accountId).get().getBalance());
    }
}
