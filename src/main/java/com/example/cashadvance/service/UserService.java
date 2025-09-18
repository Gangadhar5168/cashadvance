package com.example.cashadvance.service;

import com.example.cashadvance.model.User;
import com.example.cashadvance.repository.UserRepository;
import com.example.cashadvance.repository.RoleRepository;
import com.example.cashadvance.model.Role;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User createSupervisor(User supervisor) {
        supervisor.setPassword(passwordEncoder.encode(supervisor.getPassword()));
        supervisor.setRole("SUPERVISOR");
        return userRepository.save(supervisor);
    }

    @Transactional
    public void setMonthlyLimit(Long userId, Double limit) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setMonthlyLimit(limit);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUserWithRoles(String username, String rawPassword, Double salary, Double allowance, java.util.List<String> roleNames) {
        Set<Role> roles = roleNames == null ? java.util.Set.of() : roleNames.stream().map(rn -> roleRepository.findByName(rn).orElseGet(() -> roleRepository.save(Role.builder().name(rn).build()))).collect(Collectors.toSet());
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .salary(salary)
                .allowance(allowance)
                .roles(roles)
                .build();
        return userRepository.save(user);
    }
}