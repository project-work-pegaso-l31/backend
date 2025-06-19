package com.example.bank.controller;

import com.example.bank.dto.*;
import com.example.bank.service.CustomerService;
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

    private final CustomerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO create(@Valid @RequestBody CreateCustomerDTO body) {
        return service.create(body);
    }

    @GetMapping
    public List<CustomerDTO> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public CustomerDTO get(@PathVariable UUID id) {
        return service.get(id);
    }
}
