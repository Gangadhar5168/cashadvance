package com.example.cashadvance.controller;

import com.example.cashadvance.model.User;
import com.example.cashadvance.service.AdvanceTransactionService;
import com.example.cashadvance.service.UserService;
import com.example.cashadvance.repository.AdvanceTransactionRepository;
import com.example.cashadvance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {TransactionController.class, SupervisorController.class})
@Import(ControllerTests.TestConfig.class)
class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdvanceTransactionService transactionService;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public AdvanceTransactionService transactionService() {
            return Mockito.mock(AdvanceTransactionService.class);
        }

        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }

        @Bean
        public AdvanceTransactionRepository transactionRepository() {
            return Mockito.mock(AdvanceTransactionRepository.class);
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }

    @BeforeEach
    void setup() {
        Mockito.reset(transactionService, userService);
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    void testGetAllTransactions() throws Exception {
        Mockito.when(transactionService.getAllTransactions()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateSupervisor() throws Exception {
        User supervisor = new User();
        supervisor.setUsername("supervisor1");
        supervisor.setPassword("password");
        supervisor.setRole("SUPERVISOR");

        Mockito.when(userService.createSupervisor(Mockito.any(User.class))).thenReturn(supervisor);

    mockMvc.perform(post("/api/supervisors")
            .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"supervisor1\",\"password\":\"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("supervisor1"))
                .andExpect(jsonPath("$.role").value("SUPERVISOR"));
    }
}