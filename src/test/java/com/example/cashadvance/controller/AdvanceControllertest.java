package com.example.cashadvance.controller;

import com.example.cashadvance.dto.AdvanceRequest;
import com.example.cashadvance.model.*;
import com.example.cashadvance.repository.MonthlyLimitRepository;
import com.example.cashadvance.repository.UserRepository;
import com.example.cashadvance.service.AdvanceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(AdvanceController.class)
class AdvanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdvanceService advanceService;

    @MockBean
    private MonthlyLimitRepository monthlyLimitRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testGiveAdvanceEndpoint() throws Exception {
        User user = User.builder().id(1L).username("employee").build();
        User supervisor = User.builder().id(2L).username("supervisor").build();
        String monthYear = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        MonthlyLimit limit = MonthlyLimit.builder()
            .user(user)
            .monthYear(monthYear)
            .originalLimit(10000.0)
            .remainingLimit(8000.0)
            .carriedDebt(0.0)
            .build();

        AdvanceTransaction tx = AdvanceTransaction.builder()
            .id(100L)
            .user(user)
            .amount(2000.0)
            .type(AdvanceTransaction.TransactionType.ADVANCE)
            .approvedBy(supervisor)
            .timestamp(LocalDateTime.now())
            .build();

        Mockito.when(advanceService.giveAdvance(eq(1L), eq(2000.0), eq(2L))).thenReturn(tx);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(monthlyLimitRepository.findByUserAndMonthYear(user, monthYear)).thenReturn(Optional.of(limit));

        String json = "{\"userId\":1,\"amount\":2000}";

        mockMvc.perform(post("/api/advances?supervisorId=2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }
}
