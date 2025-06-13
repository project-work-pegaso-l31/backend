package com.example.bank.controller;

import com.example.bank.dto.TransactionDTO;
import com.example.bank.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Tag(name = "Transactions")
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping("/{accountId}/credit")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDTO credit(@PathVariable UUID accountId,
                                 @RequestParam BigDecimal amount,
                                 @RequestParam(required = false) String description) {
        return service.credit(accountId, amount, description);
    }

    @PostMapping("/{accountId}/debit")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDTO debit(@PathVariable UUID accountId,
                                @RequestParam BigDecimal amount,
                                @RequestParam(required = false) String description) {
        return service.debit(accountId, amount, description);
    }

    @GetMapping("/{accountId}")
    public List<TransactionDTO> list(@PathVariable UUID accountId) {
        return service.listByAccount(accountId);
    }
}
