package com.example.cashadvance.mapper;

import com.example.cashadvance.dto.UserResponse;
import com.example.cashadvance.model.Role;
import com.example.cashadvance.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(User u) {
        if (u == null) return null;
        List<String> roleNames = null;
        if (u.getRoles() != null) {
            roleNames = u.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        }
        return UserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .salary(u.getSalary())
                .allowance(u.getAllowance())
                .monthlyLimit(u.getMonthlyLimit())
                .roles(roleNames)
                .build();
    }
}
