package com.example.cashadvance.controller;

import com.example.cashadvance.model.AdvanceTransaction;
import com.example.cashadvance.model.User;
import com.example.cashadvance.service.AdvanceTransactionService;
import com.example.cashadvance.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ManagerController {

    private final AdvanceTransactionService transactionService;
    private final UserService userService;

    public ManagerController(AdvanceTransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/ui/manager/overview")
    public String overview(Model model) {
        List<AdvanceTransaction> transactions = transactionService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        model.addAttribute("supervisor", new User());
        return "manager/overview";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/ui/manager/supervisors/create")
    public String createSupervisor(@ModelAttribute("supervisor") User supervisor, Model model) {
        // UserService.createSupervisor sets role and encodes password
        try {
            User created = userService.createSupervisor(supervisor);
            model.addAttribute("createdSupervisor", created);
            model.addAttribute("successMessage", "Supervisor created: " + created.getUsername());
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        // refresh transactions and show the overview again
        List<AdvanceTransaction> transactions = transactionService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "manager/overview";
    }
}
