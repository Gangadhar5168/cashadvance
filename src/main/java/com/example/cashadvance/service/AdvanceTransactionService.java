package com.example.cashadvance.service;

import com.example.cashadvance.model.AdvanceTransaction;
import com.example.cashadvance.repository.AdvanceTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvanceTransactionService {
    private final AdvanceTransactionRepository advanceTransactionRepository;

    public AdvanceTransactionService(AdvanceTransactionRepository advanceTransactionRepository) {
        this.advanceTransactionRepository = advanceTransactionRepository;
    }

    public AdvanceTransaction saveTransaction(AdvanceTransaction transaction) {
        return advanceTransactionRepository.save(transaction);
    }

    public AdvanceTransaction cancelTransaction(Long id, com.example.cashadvance.model.User admin) {
        AdvanceTransaction tx = advanceTransactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        tx.setStatus(AdvanceTransaction.TransactionStatus.CANCELLED);
        tx.setApprovedBy(admin);
        return advanceTransactionRepository.save(tx);
    }

    public AdvanceTransaction overrideTransaction(Long id, com.example.cashadvance.model.User admin) {
        AdvanceTransaction tx = advanceTransactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        tx.setStatus(AdvanceTransaction.TransactionStatus.OVERRIDDEN);
        tx.setApprovedBy(admin);
        return advanceTransactionRepository.save(tx);
    }

    public List<AdvanceTransaction> getAllTransactions() {
        return advanceTransactionRepository.findAll();
    }
}
