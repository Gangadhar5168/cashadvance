package com.example.cashadvance.controller;

import com.example.cashadvance.model.User;
import com.example.cashadvance.dto.UserResponse;
import com.example.cashadvance.mapper.UserMapper;
import com.example.cashadvance.service.UserService;
import com.example.cashadvance.service.AdvanceTransactionService;
import com.example.cashadvance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdvanceTransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{userId}/monthly-limit")
    public ResponseEntity<String> setMonthlyLimit(@PathVariable Long userId, @RequestParam Double limit) {
        userService.setMonthlyLimit(userId, limit);
        return ResponseEntity.ok("Monthly limit updated successfully.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> listAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> responses = users.stream().map(userMapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody com.example.cashadvance.dto.CreateUserRequest req) {
        User created = userService.createUserWithRoles(req.getUsername(), req.getPassword(), req.getSalary(), req.getAllowance(), req.getRoles());
        return ResponseEntity.ok(userMapper.toResponse(created));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/transactions/{id}/cancel")
    public ResponseEntity<String> cancelTransaction(@PathVariable Long id, java.security.Principal principal) {
        com.example.cashadvance.model.User admin = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
        transactionService.cancelTransaction(id, admin);
        return ResponseEntity.ok("Transaction cancelled.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/transactions/{id}/override")
    public ResponseEntity<String> overrideTransaction(@PathVariable Long id, java.security.Principal principal) {
        com.example.cashadvance.model.User admin = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
        transactionService.overrideTransaction(id, admin);
        return ResponseEntity.ok("Transaction overridden.");
    }
}