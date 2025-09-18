package com.example.cashadvance.service;

import com.example.cashadvance.model.*;
import com.example.cashadvance.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class AdvanceServiceTest {

    @Test
    void testGiveAdvanceHappyPath() {
        MonthlyLimitRepository limitRepo = Mockito.mock(MonthlyLimitRepository.class);
        AdvanceTransactionRepository txRepo = Mockito.mock(AdvanceTransactionRepository.class);
        UserRepository userRepo = Mockito.mock(UserRepository.class);

        AdvanceService service = new AdvanceService(limitRepo, txRepo, userRepo);

        User user = User.builder().id(1L).username("employee").build();
        User supervisor = User.builder().id(2L).username("supervisor").build();
        String monthYear = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
        MonthlyLimit limit = MonthlyLimit.builder()
            .user(user)
            .monthYear(monthYear)
            .originalLimit(10000.0)
            .remainingLimit(5000.0)
            .carriedDebt(0.0)
            .build();

        Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepo.findById(2L)).thenReturn(Optional.of(supervisor));
        Mockito.when(limitRepo.findByUserAndMonthYear(user, monthYear)).thenReturn(Optional.of(limit));
        Mockito.when(limitRepo.save(Mockito.any())).thenReturn(limit);

        AdvanceTransaction tx = AdvanceTransaction.builder()
            .user(user)
            .amount(2000.0)
            .type(AdvanceTransaction.TransactionType.ADVANCE)
            .approvedBy(supervisor)
            .timestamp(LocalDateTime.now())
            .build();

        Mockito.when(txRepo.save(Mockito.any())).thenReturn(tx);

        AdvanceTransaction result = service.giveAdvance(1L, 2000.0, 2L);

        assertEquals(2000.0, result.getAmount());
        assertEquals(3000.0, limit.getRemainingLimit());
    }

    @Test
    void testGiveAdvanceLimitExceeded() {
        MonthlyLimitRepository limitRepo = Mockito.mock(MonthlyLimitRepository.class);
        AdvanceTransactionRepository txRepo = Mockito.mock(AdvanceTransactionRepository.class);
        UserRepository userRepo = Mockito.mock(UserRepository.class);

        AdvanceService service = new AdvanceService(limitRepo, txRepo, userRepo);

        User user = User.builder().id(1L).username("employee").build();
        User supervisor = User.builder().id(2L).username("supervisor").build();
        String monthYear = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
        MonthlyLimit limit = MonthlyLimit.builder()
            .user(user)
            .monthYear(monthYear)
            .originalLimit(10000.0)
            .remainingLimit(1000.0)
            .carriedDebt(0.0)
            .build();

        Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepo.findById(2L)).thenReturn(Optional.of(supervisor));
        Mockito.when(limitRepo.findByUserAndMonthYear(user, monthYear)).thenReturn(Optional.of(limit));

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
            service.giveAdvance(1L, 2000.0, 2L)
        );
        assertEquals("Advance amount exceeds remaining limit", ex.getMessage());
    }
}
