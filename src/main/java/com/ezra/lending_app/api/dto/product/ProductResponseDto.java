package com.ezra.lending_app.api.dto.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.ezra.lending_app.domain.enums.LoanTerm;
import com.ezra.lending_app.domain.enums.ProductLoanTenure;
import com.ezra.lending_app.domain.enums.ProductStatus;
import com.ezra.lending_app.domain.enums.RepaymentFrequencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponseDto {
    private String code;
    private String name;
    private ProductLoanTenure loanTenure;
    private String description;
    private String currency;
    private BigDecimal minLoanAmount;
    private BigDecimal maxLoanAmount;
    private int minLoanTermDuration;
    private LoanTerm minLoanTermType;
    private int maxLoanTermDuration;
    private LoanTerm maxLoanTermType;
    private List<ProductFeeDto> fees;
    private ProductStatus status;
    private RepaymentFrequencyType repaymentFrequency;
    private int gracePeriodAfterLoanDueDateInDays;
    private int gracePeriodBeforeLoanDueDateInDays;
    private Instant dateCreated;
    private Instant dateModified;
}
