package com.example.bank;

import com.example.bank.domain.Customer;
import com.example.bank.dto.CreateAccountDTO;
import com.example.bank.exception.NotFoundException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.CustomerRepository;
import com.example.bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountServiceTest {

    private AccountService accountSvc;
    private AccountRepository accRepo;
    private CustomerRepository custRepo;

    private UUID customerId;

    @BeforeEach
    void setUp() {
        accRepo  = new AccountRepository();
        custRepo = new CustomerRepository();
        accountSvc = new AccountService(accRepo, custRepo);

        // cliente valido
        customerId = custRepo.save(
                Customer.builder()
                        .fullName("Mario Rossi")
                        .email("mario@ex.com")
                        .fiscalCode("RSSMRA80A01H501U")
                        .build()
        ).getId();
    }

    @Test
    void open_account_ok() {
        var dto = new CreateAccountDTO(customerId, "IT60X0542811101000000123456");
        var acc = accountSvc.openAccount(dto);
        assertEquals(dto.iban(), acc.iban());
        assertEquals(customerId,  acc.customerId());
    }

    @Test
    void duplicate_iban_throws() {
        String iban = "IT60X0542811101000000123456";
        accountSvc.openAccount(new CreateAccountDTO(customerId, iban));
        assertThrows(IllegalArgumentException.class,
                () -> accountSvc.openAccount(new CreateAccountDTO(customerId, iban)));
    }

    @Test
    void unknown_customer_throws() {
        UUID wrongCust = UUID.randomUUID();
        assertThrows(NotFoundException.class,
                () -> accountSvc.openAccount(new CreateAccountDTO(wrongCust, "IT60X...")));
    }
}
