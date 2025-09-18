package com.example.cashadvance.controller;

import com.example.cashadvance.dto.CreateUserRequest;
import com.example.cashadvance.dto.UserResponse;
import com.example.cashadvance.mapper.UserMapper;
import com.example.cashadvance.model.User;
import com.example.cashadvance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import java.util.List;

@Controller
public class ThymeleafAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ui/admin/users")
    public String users(Model model) {
        List<User> users = userService.getAllUsers();
        List<UserResponse> responses = users.stream().map(userMapper::toResponse).toList();
        model.addAttribute("users", responses);
        return "admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ui/admin/users/create")
    public String createForm(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());
        return "admin/create-user";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/ui/admin/users/create")
    public String createSubmit(@Valid @ModelAttribute("createUserRequest") CreateUserRequest req,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/create-user";
        }
    // Roles are bound directly from the form checkboxes to req.roles

        User created = userService.createUserWithRoles(req.getUsername(), req.getPassword(), req.getSalary(), req.getAllowance(), req.getRoles());
        model.addAttribute("user", userMapper.toResponse(created));
        return "admin/create-user-success";
    }
}
