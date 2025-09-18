package com.example.cashadvance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityUiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "adminuser", roles = {"ADMIN"})
    void adminShouldSeeAdminMenuItems() throws Exception {
        mockMvc.perform(get("/ui/admin/users"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Create User")))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Users")));
    }

    @Test
    @WithMockUser(username = "manageruser", roles = {"MANAGER"})
    void managerShouldSeeManagerMenuButNotCreateUser() throws Exception {
        mockMvc.perform(get("/ui/manager/overview"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Manager")));

        mockMvc.perform(get("/ui/admin/users"))
            .andExpect(status().isForbidden());
    }
}
