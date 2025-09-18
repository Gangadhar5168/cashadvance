package com.example.cashadvance.service;

import com.example.cashadvance.model.MonthlyLimit;
import com.example.cashadvance.model.User;
import com.example.cashadvance.repository.MonthlyLimitRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MonthlyLimitServiceTest {

    @Test
    void testCreateOrUpdateLimit() {
        MonthlyLimitRepository repo = Mockito.mock(MonthlyLimitRepository.class);
        MonthlyLimitService service = new MonthlyLimitService(repo);

        User user = User.builder().id(1L).username("testuser").build();
        String monthYear = "2025-09";
        Double originalLimit = 10000.0;
        Double carriedDebt = 2000.0;

        MonthlyLimit limit = MonthlyLimit.builder()
            .user(user)
            .monthYear(monthYear)
            .originalLimit(originalLimit)
            .carriedDebt(carriedDebt)
            .remainingLimit(originalLimit - carriedDebt)
            .build();

        Mockito.when(repo.findByUserAndMonthYear(user, monthYear)).thenReturn(Optional.empty());
        Mockito.when(repo.save(Mockito.any(MonthlyLimit.class))).thenReturn(limit);

        MonthlyLimit result = service.createOrUpdateLimit(user, monthYear, originalLimit, carriedDebt);

        assertEquals(originalLimit, result.getOriginalLimit());
        assertEquals(carriedDebt, result.getCarriedDebt());
        assertEquals(originalLimit - carriedDebt, result.getRemainingLimit());
    }
}
