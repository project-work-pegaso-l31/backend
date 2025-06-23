package com.example.bank.controller;

import com.example.bank.domain.Customer;
import com.example.bank.dto.CreateCustomerDTO;
import com.example.bank.exception.NotFoundException;
import com.example.bank.repository.CustomerRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Customers")
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository repo;

    /* ---- lista completa ---- */
    @GetMapping
    public List<Customer> list() {
        return repo.findAll();
    }

    /* ---- ricerca per ID ---- */
    @GetMapping("/{id}")
    public Customer get(@PathVariable UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente non trovato"));
    }

    /* ---- ricerca per Codice Fiscale ---- */
    @GetMapping("/by-fiscalCode")
    public Customer getByFiscalCode(@RequestParam String cf) {
        return repo.findByFiscalCodeIgnoreCase(cf)
                .orElseThrow(() -> new NotFoundException("Cliente non trovato"));
    }

    /* ---- crea cliente ---- */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@Valid @RequestBody CreateCustomerDTO body) {
        var c = Customer.builder()
                .fullName(body.fullName())
                .email(body.email())
                .fiscalCode(body.fiscalCode())
                .build();                    // id e accounts vengono gestiti da save()
        return repo.save(c);
    }
}
