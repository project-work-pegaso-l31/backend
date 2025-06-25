package com.example.bank.controller;

import com.example.bank.dto.TransactionDTO;
import com.example.bank.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Tag(name = "Transactions")
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping("/{accId}/credit")
    public TransactionDTO credit(@PathVariable UUID accId,
                                 @RequestParam BigDecimal amount,
                                 @RequestParam(required = false) String description) {
        return service.credit(accId, amount, description);
    }

    @PostMapping("/{accId}/debit")
    public TransactionDTO debit(@PathVariable UUID accId,
                                @RequestParam BigDecimal amount,
                                @RequestParam(required = false) String description) {
        return service.debit(accId, amount, description);
    }

    @GetMapping("/{accId}")
    public List<TransactionDTO> list(@PathVariable UUID accId) {
        return service.listByAccount(accId);
    }
}
