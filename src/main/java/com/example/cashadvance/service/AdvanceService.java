package com.example.cashadvance.service;

import com.example.cashadvance.model.*;
import com.example.cashadvance.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AdvanceService {
    private final MonthlyLimitRepository monthlyLimitRepository;
    private final AdvanceTransactionRepository advanceTransactionRepository;
    private final UserRepository userRepository;

    public AdvanceService(MonthlyLimitRepository monthlyLimitRepository,
                         AdvanceTransactionRepository advanceTransactionRepository,
                         UserRepository userRepository) {
        this.monthlyLimitRepository = monthlyLimitRepository;
        this.advanceTransactionRepository = advanceTransactionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AdvanceTransaction giveAdvance(Long userId, Double amount, Long supervisorId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User supervisor = userRepository.findById(supervisorId)
            .orElseThrow(() -> new IllegalArgumentException("Supervisor not found"));

        String monthYear = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        MonthlyLimit limit = monthlyLimitRepository.findByUserAndMonthYear(user, monthYear)
            .orElseThrow(() -> new IllegalArgumentException("Monthly limit not found"));

        if (amount > limit.getRemainingLimit()) {
            throw new IllegalArgumentException("Advance amount exceeds remaining limit");
        }

        limit.setRemainingLimit(limit.getRemainingLimit() - amount);
        monthlyLimitRepository.save(limit);

        AdvanceTransaction transaction = AdvanceTransaction.builder()
            .user(user)
            .amount(amount)
            .type(AdvanceTransaction.TransactionType.ADVANCE)
            .approvedBy(supervisor)
            .timestamp(LocalDateTime.now())
            .build();

        return advanceTransactionRepository.save(transaction);
    }
}
