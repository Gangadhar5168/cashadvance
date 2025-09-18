package com.example.cashadvance.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyLimit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String monthYear; // Format: "2025-09"

    private Double originalLimit;
    private Double remainingLimit;
    private Double carriedDebt;
}
