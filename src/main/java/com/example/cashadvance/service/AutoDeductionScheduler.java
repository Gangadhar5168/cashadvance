package com.example.cashadvance.service;

import com.example.cashadvance.repository.MonthlyLimitRepository;
import com.example.cashadvance.model.MonthlyLimit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutoDeductionScheduler {

    private final MonthlyLimitRepository limitRepo;

    public AutoDeductionScheduler(MonthlyLimitRepository limitRepo) {
        this.limitRepo = limitRepo;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void autoDeduct() {
        List<MonthlyLimit> limits = limitRepo.findAll();

        for (MonthlyLimit limit : limits) {
            double deductionAmount = 100.0; // Example fixed deduction amount
            if (limit.getRemainingLimit() >= deductionAmount) {
                limit.setRemainingLimit(limit.getRemainingLimit() - deductionAmount);
                limitRepo.save(limit);
            } else {
                System.out.println("Insufficient limit for user: " + limit.getUser().getUsername());
            }
        }
    }
}