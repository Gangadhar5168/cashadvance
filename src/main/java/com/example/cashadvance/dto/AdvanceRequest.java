package com.example.cashadvance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AdvanceRequest {
    @NotNull
    private Long userId;

    @NotNull
    @Positive
    private Double amount;
}
