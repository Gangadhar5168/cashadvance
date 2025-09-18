package com.example.cashadvance.controller;

import com.example.cashadvance.model.User;
import com.example.cashadvance.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/supervisors")
public class SupervisorController {

    private final UserService userService;

    public SupervisorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<User> createSupervisor(@RequestBody User supervisor) {
        User createdSupervisor = userService.createSupervisor(supervisor);
        return new ResponseEntity<>(createdSupervisor, HttpStatus.CREATED);
    }
}