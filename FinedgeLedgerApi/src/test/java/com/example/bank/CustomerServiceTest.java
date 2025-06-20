package com.example.bank;

import com.example.bank.dto.CreateCustomerDTO;
import com.example.bank.repository.CustomerRepository;
import com.example.bank.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerServiceTest {

    private CustomerService custSvc;

    @BeforeEach
    void setUp() {
        custSvc = new CustomerService(new CustomerRepository());
    }

    @Test
    void create_and_retrieve_customer() {
        var dtoIn = new CreateCustomerDTO(
                "Mario Rossi", "mario@ex.com", "RSSMRA80A01H501U");
        var created = custSvc.create(dtoIn);

        assertNotNull(created.id());
        assertEquals("Mario Rossi", created.fullName());

        var fetched = custSvc.get(created.id());
        assertEquals(created, fetched);
    }
}
