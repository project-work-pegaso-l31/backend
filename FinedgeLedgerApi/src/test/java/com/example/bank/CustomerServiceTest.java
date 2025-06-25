package com.example.bank;

import com.example.bank.dto.CreateCustomerDTO;
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
    void create_and_retrieve_customer() {
        var dtoIn = new CreateCustomerDTO(
                "Mario Rossi", "mario@ex.com", "RSSMRA80A01H501U");
        var created = custSvc.create(dtoIn);

        assertNotNull(created.id());
        assertEquals("Mario Rossi", created.fullName());

        var fetched = custSvc.get(created.id());
        assertEquals(created, fetched);
    }

    @Test
    void update_customer_data() {
        var created = custSvc.create(new CreateCustomerDTO(
                "Anna Bianchi", "anna@ex.com", "BNCNNA90A01F205X"));

        var dtoUpd = new CreateCustomerDTO(
                "Anna Maria Bianchi", "anna.new@ex.com", "BNCNNA90A01F205X");

        var updated = custSvc.update(created.id(), dtoUpd);

        assertEquals("Anna Maria Bianchi", updated.fullName());
        assertEquals("anna.new@ex.com", updated.email());
    }
}
