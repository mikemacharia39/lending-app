package com.ezra.lending_app.api.dto.loan;

import com.ezra.lending_app.domain.enums.LoanState;
import com.ezra.lending_app.domain.enums.LoanTerm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoanResponseDto {
    private String code;
    private Long customerId;
    private Long productId;
    private BigDecimal requestedAmount;
    private BigDecimal disbursedAmount;
    private List<LoanFeeDto> loanFees;
    private BigDecimal fullLoanAmountPlusFees;
    private LoanTerm loanTermType;
    private int loanPeriod;
    private Instant disbursedDate;
    private Instant dueDate;
    private LoanState state;
    private List<LoanInstallmentDto> installment;
    private List<LoanRepaymentReceiptDto> repayments;
    private Instant dateCreated;
}
