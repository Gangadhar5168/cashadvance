package com.example.cashadvance.service;

import com.example.cashadvance.model.MonthlyLimit;
import com.example.cashadvance.model.User;
import com.example.cashadvance.repository.MonthlyLimitRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MonthlyLimitService {
    private final MonthlyLimitRepository monthlyLimitRepository;

    public MonthlyLimitService(MonthlyLimitRepository monthlyLimitRepository) {
        this.monthlyLimitRepository = monthlyLimitRepository;
    }

    public Optional<MonthlyLimit> getCurrentLimit(User user, String monthYear) {
        return monthlyLimitRepository.findByUserAndMonthYear(user, monthYear);
    }

    public MonthlyLimit createOrUpdateLimit(User user, String monthYear, Double originalLimit, Double carriedDebt) {
        MonthlyLimit limit = monthlyLimitRepository.findByUserAndMonthYear(user, monthYear)
            .orElse(MonthlyLimit.builder()
                .user(user)
                .monthYear(monthYear)
                .originalLimit(originalLimit)
                .carriedDebt(carriedDebt)
                .remainingLimit(originalLimit - carriedDebt)
                .build());
        limit.setOriginalLimit(originalLimit);
        limit.setCarriedDebt(carriedDebt);
        limit.setRemainingLimit(originalLimit - carriedDebt);
        return monthlyLimitRepository.save(limit);
    }
}
