package com.example.cashadvance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

// ... no special SecurityMockMvcRequestBuilders imports needed
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RememberMeFormLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void formLoginWithRememberMeShouldSetCookie() throws Exception {
        // Using formLogin helper to post credentials. The test user must exist in the in-memory test DB or be mocked.
        // We'll attempt to use an existing seeded admin user (admin/admin123) used elsewhere in tests.
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
                .param("username", "admin")
                .param("password", "admin123")
                .param("remember-me", "true")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(cookie().exists("remember-me"));
    }
}
