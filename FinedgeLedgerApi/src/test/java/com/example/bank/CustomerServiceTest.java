package com.example.bank;

import com.example.bank.dto.CreateCustomerDTO;
import com.example.bank.dto.UpdateCustomerDTO;
import com.example.bank.repository.CustomerRepository;
import com.example.bank.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private CustomerService custSvc;

    @BeforeEach
    void setUp() {
        custSvc = new CustomerService(new CustomerRepository());
    }

    @Test
    void create_and_update_customer() {
        var created = custSvc.create(new CreateCustomerDTO(
                "Mario Rossi", "mario@ex.com", "RSSMRA80A01H501U"));

        /* update */
        var upd = custSvc.update(created.id(),
                new UpdateCustomerDTO("M. Rossi", "rossi@ex.com", "RSSMRA80A01H501U"));

        assertEquals("M. Rossi", upd.fullName());
        assertEquals("rossi@ex.com", upd.email());
    }

    @Test
    void duplicate_email_throws() {
        custSvc.create(new CreateCustomerDTO(
                "A", "a@ex.com", "AAAAAA80A01H501U"));

        var c2 = custSvc.create(new CreateCustomerDTO(
                "B", "b@ex.com", "BBBBBB80A01H501U"));

        assertThrows(IllegalArgumentException.class,
                () -> custSvc.update(c2.id(),
                        new UpdateCustomerDTO("B", "a@ex.com", "BBBBBB80A01H501U")));
    }
}
