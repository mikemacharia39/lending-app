package com.ezra.lending_app.domain.entities;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import com.ezra.lending_app.domain.enums.LoanTerm;
import com.ezra.lending_app.domain.enums.ProductLoanTenure;
import com.ezra.lending_app.domain.enums.ProductStatus;
import com.ezra.lending_app.domain.enums.RepaymentFrequencyType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.ezra.lending_app.domain.util.RandomReferenceGenerator.generateReference;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @Column(nullable = false, unique = true)
    private final String code = generateReference();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "currency", length = 3, nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_tenure", nullable = false)
    private ProductLoanTenure loanTenure;

    @Column(name = "min_loan_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal minLoanAmount;

    @Column(name = "max_loan_amount", nullable = false, precision = 19, scale = 2)
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFee> fees;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.INACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "repayment_frequency", nullable = false)
    private RepaymentFrequencyType repaymentFrequency;

    @Column(name = "grace_period_after_loan_due_date_in_days", nullable = false)
    private int gracePeriodAfterLoanDueDateInDays;

    @Column(name = "grace_period_before_loan_due_date_in_days", nullable = false)
    private int gracePeriodBeforeLoanDueDateInDays;

    public void activate() {
        this.status = ProductStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = ProductStatus.DEACTIVATED;
    }

    public boolean isActive() {
        return this.status == ProductStatus.ACTIVE;
    }
}
