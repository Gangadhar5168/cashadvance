package com.example.cashadvance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdvanceResponse {
    private Long transactionId;
    private Double amount;
    private Double remainingLimit;
}
