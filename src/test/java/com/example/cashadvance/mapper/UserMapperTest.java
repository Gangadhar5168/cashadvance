package com.example.cashadvance.mapper;

import com.example.cashadvance.dto.UserResponse;
import com.example.cashadvance.model.Role;
import com.example.cashadvance.model.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void toResponse_mapsFieldsAndRoles() {
        Role r = Role.builder().id(1L).name("ADMIN").build();
        User u = User.builder()
                .id(10L)
                .username("alice")
                .password("hash")
                .salary(50000.0)
                .allowance(2000.0)
                .monthlyLimit(1000.0)
                .roles(Set.of(r))
                .build();

        UserResponse res = mapper.toResponse(u);

        assertThat(res).isNotNull();
        assertThat(res.getId()).isEqualTo(10L);
        assertThat(res.getUsername()).isEqualTo("alice");
        assertThat(res.getSalary()).isEqualTo(50000.0);
        assertThat(res.getAllowance()).isEqualTo(2000.0);
        assertThat(res.getMonthlyLimit()).isEqualTo(1000.0);
        assertThat(res.getRoles()).containsExactly("ADMIN");
    }
}
