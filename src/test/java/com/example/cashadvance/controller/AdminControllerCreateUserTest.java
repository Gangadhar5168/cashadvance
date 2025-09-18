package com.example.cashadvance.controller;

import com.example.cashadvance.model.Role;
import com.example.cashadvance.model.User;
import com.example.cashadvance.dto.CreateUserRequest;
import com.example.cashadvance.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AdminController.class)
@Import(com.example.cashadvance.mapper.UserMapper.class)
class AdminControllerCreateUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private com.example.cashadvance.service.AdvanceTransactionService transactionService;

    @MockBean
    private com.example.cashadvance.repository.UserRepository userRepository;

    @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createUser_shouldReturnCreatedUserWithoutPassword() throws Exception {
        Role role = Role.builder().id(2L).name("EMPLOYEE").build();
        User created = User.builder()
                .id(10L)
                .username("jane")
                .password("encoded")
                .salary(50000.0)
                .allowance(2000.0)
                .roles(Set.of(role))
                .build();

        when(userService.createUserWithRoles(any(), any(), any(), any(), any())).thenReturn(created);

    CreateUserRequest req = CreateUserRequest.builder()
        .username("jane")
        .password("secret1")
                .salary(50000.0)
                .allowance(2000.0)
                .roles(java.util.List.of("EMPLOYEE"))
                .build();

        mockMvc.perform(post("/api/admin/users")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.username").value("jane"));
    }
}
