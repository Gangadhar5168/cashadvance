package com.example.cashadvance.controller;

import com.example.cashadvance.model.AdvanceTransaction;
import com.example.cashadvance.model.Role;
import com.example.cashadvance.model.User;
import com.example.cashadvance.repository.UserRepository;
import com.example.cashadvance.service.AdvanceTransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@Import(com.example.cashadvance.mapper.UserMapper.class)
class AdminControllerTransactionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdvanceTransactionService transactionService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private com.example.cashadvance.service.UserService userService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void cancelTransaction_returnsOk() throws Exception {
        User admin = User.builder().id(1L).username("admin").roles(Set.of(Role.builder().id(1L).name("ADMIN").build())).build();
        AdvanceTransaction tx = AdvanceTransaction.builder().id(2L).amount(100.0).timestamp(LocalDateTime.now()).status(AdvanceTransaction.TransactionStatus.CANCELLED).build();

        when(userRepository.findByUsername("admin")).thenReturn(java.util.Optional.of(admin));
        when(transactionService.cancelTransaction(anyLong(), org.mockito.Mockito.any())).thenReturn(tx);

    mockMvc.perform(put("/api/admin/transactions/2/cancel").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void overrideTransaction_returnsOk() throws Exception {
        User admin = User.builder().id(1L).username("admin").roles(Set.of(Role.builder().id(1L).name("ADMIN").build())).build();
        AdvanceTransaction tx = AdvanceTransaction.builder().id(3L).amount(200.0).timestamp(LocalDateTime.now()).status(AdvanceTransaction.TransactionStatus.OVERRIDDEN).build();

        when(userRepository.findByUsername("admin")).thenReturn(java.util.Optional.of(admin));
        when(transactionService.overrideTransaction(anyLong(), org.mockito.Mockito.any())).thenReturn(tx);

    mockMvc.perform(put("/api/admin/transactions/3/override").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().isOk());
    }
}
