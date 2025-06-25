package com.example.bank;

import com.example.bank.dto.CreateCustomerDTO;
import com.example.bank.dto.UpdateCustomerDTO;
import com.example.bank.exception.DuplicateEmailException;
import com.example.bank.exception.DuplicateFiscalCodeException;
import com.example.bank.repository.CustomerRepository;
import com.example.bank.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di unità per {@link CustomerService}.
 * – Coprono creazione, lettura, aggiornamento e gestione duplicati.
 */
class CustomerServiceTest {

    private CustomerService service;

    @BeforeEach
    void setUp() {
        service = new CustomerService(new CustomerRepository());
    }

    /* ---------------------------------------------------------------------- */
    /* ----------------------------  CREATE  -------------------------------- */
    /* ---------------------------------------------------------------------- */

    @Test
    void create_and_retrieve_customer() {
        var dtoIn = new CreateCustomerDTO(
                "Mario Rossi", "mario@ex.com", "RSSMRA80A01H501U");

        var created = service.create(dtoIn);

        assertNotNull(created.id());
        assertEquals(dtoIn.fullName(),  created.fullName());
        assertEquals(dtoIn.email(),     created.email());
        assertEquals(dtoIn.fiscalCode(),created.fiscalCode());

        // round-trip
        var fetched = service.get(created.id());
        assertEquals(created, fetched);
    }

    @Test
    void duplicate_email_throws_on_create() {
        service.create(new CreateCustomerDTO(
                "Mario Rossi", "mario@ex.com", "RSSMRA80A01H501U"));

        assertThrows(DuplicateEmailException.class, () ->
                service.create(new CreateCustomerDTO(
                        "Gianni Verdi", "mario@ex.com", "VRDGNN75H06F205Z")));
    }

    @Test
    void duplicate_cf_throws_on_create() {
        service.create(new CreateCustomerDTO(
                "Mario Rossi", "mario@ex.com", "RSSMRA80A01H501U"));

        assertThrows(DuplicateFiscalCodeException.class, () ->
                service.create(new CreateCustomerDTO(
                        "Gianni Verdi", "gianni@ex.com", "RSSMRA80A01H501U")));
    }

    /* ---------------------------------------------------------------------- */
    /* ----------------------------  UPDATE  -------------------------------- */
    /* ---------------------------------------------------------------------- */

    @Test
    void update_customer_data() {
        var created = service.create(new CreateCustomerDTO(
                "Mario Rossi", "mario@ex.com", "RSSMRA80A01H501U"));

        var updated = service.update(created.id(), new UpdateCustomerDTO(
                "Mario Rossi Jr.", "mariojr@ex.com", "RSSMRA80A01H501U"));

        assertEquals("Mario Rossi Jr.", updated.fullName());
        assertEquals("mariojr@ex.com",  updated.email());
        assertEquals(created.fiscalCode(), updated.fiscalCode());
    }

    @Test
    void duplicate_email_throws_on_update() {
        var c1 = service.create(new CreateCustomerDTO(
                "Mario Rossi", "mario@ex.com",  "RSSMRA80A01H501U"));
        var c2 = service.create(new CreateCustomerDTO(
                "Gianni Verdi","gianni@ex.com", "VRDGNN75H06F205Z"));

        assertThrows(DuplicateEmailException.class, () ->
                service.update(c2.id(), new UpdateCustomerDTO(
                        "Gianni Verdi", "mario@ex.com", "VRDGNN75H06F205Z")));
    }

    @Test
    void duplicate_cf_throws_on_update() {
        var c1 = service.create(new CreateCustomerDTO(
                "Mario Rossi", "mario@ex.com",  "RSSMRA80A01H501U"));
        var c2 = service.create(new CreateCustomerDTO(
                "Gianni Verdi","gianni@ex.com", "VRDGNN75H06F205Z"));

        assertThrows(DuplicateFiscalCodeException.class, () ->
                service.update(c2.id(), new UpdateCustomerDTO(
                        "Gianni Verdi", "gianni@ex.com", "RSSMRA80A01H501U")));
    }
}
