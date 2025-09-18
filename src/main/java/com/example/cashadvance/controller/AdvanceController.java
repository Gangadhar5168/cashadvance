package com.example.cashadvance.controller;

import com.example.cashadvance.dto.AdvanceRequest;
import com.example.cashadvance.dto.AdvanceResponse;
import com.example.cashadvance.model.AdvanceTransaction;
import com.example.cashadvance.model.MonthlyLimit;
import com.example.cashadvance.model.User;
import com.example.cashadvance.repository.MonthlyLimitRepository;
import com.example.cashadvance.repository.UserRepository;
import com.example.cashadvance.service.AdvanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/advances")
public class AdvanceController {

    private final AdvanceService advanceService;
    private final MonthlyLimitRepository monthlyLimitRepository;
    private final UserRepository userRepository;

    public AdvanceController(AdvanceService advanceService,
                             MonthlyLimitRepository monthlyLimitRepository,
                             UserRepository userRepository) {
        this.advanceService = advanceService;
        this.monthlyLimitRepository = monthlyLimitRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERVISOR','ADMIN')")
    public ResponseEntity<AdvanceResponse> giveAdvance(@Valid @RequestBody AdvanceRequest req,
                                                       @RequestParam Long supervisorId) {
        AdvanceTransaction tx = advanceService.giveAdvance(req.getUserId(), req.getAmount(), supervisorId);

        User user = userRepository.findById(req.getUserId()).orElseThrow();
        String monthYear = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        MonthlyLimit limit = monthlyLimitRepository.findByUserAndMonthYear(user, monthYear).orElseThrow();

        AdvanceResponse resp = AdvanceResponse.builder()
                .transactionId(tx.getId())
                .amount(tx.getAmount())
                .remainingLimit(limit.getRemainingLimit())
                .build();

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/returns")
    @PreAuthorize("hasAnyRole('SUPERVISOR','ADMIN')")
    public ResponseEntity<AdvanceResponse> returnCash(@Valid @RequestBody AdvanceRequest req,
                                                       @RequestParam Long supervisorId) {
        AdvanceTransaction tx = advanceService.returnCash(req.getUserId(), req.getAmount(), supervisorId);

        User user = userRepository.findById(req.getUserId()).orElseThrow();
        String monthYear = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        MonthlyLimit limit = monthlyLimitRepository.findByUserAndMonthYear(user, monthYear).orElseThrow();

        AdvanceResponse resp = AdvanceResponse.builder()
                .transactionId(tx.getId())
                .amount(tx.getAmount())
                .remainingLimit(limit.getRemainingLimit())
                .build();

        return ResponseEntity.ok(resp);
    }
}
