package com.ezra.lending_app.api.dto.loan;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class LoanRepaymentResponseDto {
    private String code;
    private BigDecimal amount;
    private String paymentChannel;
    private String paymentReference;
    private Instant paymentDate;
}
