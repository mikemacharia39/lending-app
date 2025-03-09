package com.ezra.lending_app.api.dto;

import java.math.BigDecimal;
import java.util.List;

import com.ezra.lending_app.domain.enums.LoanTerm;
import com.ezra.lending_app.domain.enums.ProductStatus;
import com.ezra.lending_app.domain.enums.RepaymentFrequencyType;

public class ProductResponseDto {
    private String code;
    private String name;
    private String description;
    private BigDecimal interestRate;
    private String currency;
    private BigDecimal maxLoanAmount;
    private int minLoanTermDuration;
    private LoanTerm minLoanTermType;
    private int maxLoanTermDuration;
    private LoanTerm maxLoanTermType;
    private List<FeeDto> fees;
    private ProductStatus status;
    private RepaymentFrequencyType repaymentFrequency;
    private int gracePeriodAfterLoanDueDateInDays;
}
