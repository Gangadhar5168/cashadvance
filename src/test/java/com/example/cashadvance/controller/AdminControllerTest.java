package com.example.cashadvance.controller;

import com.example.cashadvance.model.Role;
import com.example.cashadvance.model.User;
import com.example.cashadvance.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@Import(com.example.cashadvance.mapper.UserMapper.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private com.example.cashadvance.service.AdvanceTransactionService transactionService;

    @MockBean
    private com.example.cashadvance.repository.UserRepository userRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void listAllUsers_shouldNotExposePassword() throws Exception {
        Role adminRole = Role.builder().id(1L).name("ADMIN").build();
        User u1 = User.builder()
                .id(1L)
                .username("admin")
                .password("secret-hash")
                .salary(100000.0)
                .allowance(5000.0)
                .monthlyLimit(null)
                .roles(Set.of(adminRole))
                .build();

        when(userService.getAllUsers()).thenReturn(List.of(u1));

        mockMvc.perform(get("/api/admin/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // ensure password field is not present
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[0].username").value("admin"));
    }
}
