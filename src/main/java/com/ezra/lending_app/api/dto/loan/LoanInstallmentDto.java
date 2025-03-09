package com.ezra.lending_app.api.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoanInstallmentDto {
    private String code;
    private BigDecimal amount;
    private Instant dueDate;
}
