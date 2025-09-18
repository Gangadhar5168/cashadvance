package com.example.cashadvance.controller;

import com.example.cashadvance.model.AdvanceTransaction;
import com.example.cashadvance.service.AdvanceTransactionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final AdvanceTransactionService transactionService;

    public TransactionController(AdvanceTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public List<AdvanceTransaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
}