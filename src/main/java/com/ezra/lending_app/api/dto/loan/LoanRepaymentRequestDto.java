package com.ezra.lending_app.api.dto.loan;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record LoanRepaymentRequestDto(
        @NotNull
        @Min(value = 1, message = "Amount to repay must be greater than 1")
        BigDecimal amount,
        @NotNull
        String paymentChannel,
        @NotNull
        String paymentReference,
        @NotNull
        Instant paymentDate
) {
}
