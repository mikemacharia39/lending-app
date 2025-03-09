package com.ezra.lending_app.domain.entities;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import com.ezra.lending_app.domain.enums.LoanTerm;
import com.ezra.lending_app.domain.enums.ProductStatus;
import com.ezra.lending_app.domain.enums.RepaymentFrequencyType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import static com.ezra.lending_app.domain.util.RandomReferenceGenerator.generateReference;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @Column(nullable = false, unique = true)
    private final String code = generateReference();

    @Column(nullable = false)
    private String name;

    private String description;

    private BigDecimal interestRate;

    @Column(name = "currency", length = 3, nullable = false)
    private Currency currency;

    @Column(name = "min_loan_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal maxLoanAmount;

    @Column(name = "min_loan_term_duration", nullable = false)
    private int minLoanTermDuration;

    @Enumerated(EnumType.STRING)
    @Column(name = "min_loan_term_type", nullable = false)
    private LoanTerm minLoanTermType;

    @Column(name = "max_loan_term_duration", nullable = false)
    private int maxLoanTermDuration;

    @Enumerated(EnumType.STRING)
    @Column(name = "max_loan_term_type", nullable = false)
    private LoanTerm maxLoanTermType;

    private List<Fee> fees;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.INACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "repayment_frequency", nullable = false)
    private RepaymentFrequencyType repaymentFrequency;

    @Column(name = "grace_period_after_loan_due_date_in_days", nullable = false)
    private int gracePeriodAfterLoanDueDateInDays;
}
