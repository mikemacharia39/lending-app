package com.ezra.lending_app.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record LoanRequestDto(
        Long customerId,
        Long productId,
        @NotNull
        @Min(value = 1, message = "The loan amount must be greater than or equal to 1")
        BigDecimal requestedAmount,
        @NotNull
        int loanPeriod
) {
}
