package com.example.bank.controller;

import com.example.bank.dto.*;
import com.example.bank.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Accounts")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO open(@Valid @RequestBody CreateAccountDTO body) {
        return service.openAccount(body);
    }

    @GetMapping("/{id}")
    public AccountDTO get(@PathVariable UUID id) {
        return service.get(id);
    }

    @GetMapping("/by-customer/{customerId}")
    public List<AccountDTO> listByCustomer(@PathVariable UUID customerId) {
        return service.listByCustomer(customerId);
    }
}
