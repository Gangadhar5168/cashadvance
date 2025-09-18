package com.example.cashadvance.service;

import com.example.cashadvance.model.AdvanceTransaction;
import com.example.cashadvance.repository.AdvanceTransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class AdvanceTransactionService {
    private final AdvanceTransactionRepository advanceTransactionRepository;

    public AdvanceTransactionService(AdvanceTransactionRepository advanceTransactionRepository) {
        this.advanceTransactionRepository = advanceTransactionRepository;
    }

    public AdvanceTransaction saveTransaction(AdvanceTransaction transaction) {
        return advanceTransactionRepository.save(transaction);
    }
}
