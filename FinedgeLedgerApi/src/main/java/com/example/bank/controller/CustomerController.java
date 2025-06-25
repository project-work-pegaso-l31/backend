package com.example.bank.controller;

import com.example.bank.dto.CreateCustomerDTO;
import com.example.bank.dto.CustomerDTO;
import com.example.bank.dto.UpdateCustomerDTO;
import com.example.bank.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    /* ---------- CRUD base ---------- */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO create(@Valid @RequestBody CreateCustomerDTO body) {
        return service.create(body);
    }

    @GetMapping
    public List<CustomerDTO> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CustomerDTO get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public CustomerDTO update(@PathVariable UUID id,
                              @Valid @RequestBody UpdateCustomerDTO body) {
        return service.update(id, body);
    }

    /* ---------- lookup per Codice Fiscale ---------- */

    /** Variante con query-string (resta compatibile). */
    @GetMapping("/by-fiscalCode")
    public CustomerDTO byCfQuery(@RequestParam String cf) {
        return service.getByFiscalCode(cf);
    }

    /** Variante usata ora dal front-end: /by-fiscalCode/{cf} */
    @GetMapping("/by-fiscalCode/{cf}")
    public CustomerDTO byCfPath(@PathVariable String cf) {
        return service.getByFiscalCode(cf);
    }
}
