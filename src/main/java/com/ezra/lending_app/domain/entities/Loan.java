package com.ezra.lending_app.domain.entities;

import com.ezra.lending_app.domain.enums.LoanState;
import com.ezra.lending_app.domain.enums.LoanTerm;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static com.ezra.lending_app.domain.util.RandomReferenceGenerator.generateReference;

@Entity
@Table(name = "customer_loan")
public class Loan extends BaseEntity {
    @Column(nullable = false, unique = true)
    private final String code = generateReference();

    @JoinColumn(name = "customer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "requested_amount", nullable = false)
    private BigDecimal requestedAmount;

    @Column(name = "disbursed_amount", nullable = false)
    private BigDecimal disbursedAmount;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanFee> loanFees;

    @Column(name = "full_loan_amount_plus_fees", nullable = false)
    private BigDecimal fullLoanAmountPlusFees;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_term_type", nullable = false)
    private LoanTerm loanTermType;

    @Column(name = "term_duration", nullable = false)
    private int loanPeriod;

    @Column(name = "disbursed_date", nullable = false)
    private Instant disbursedDate;

    @Column(name = "due_date", nullable = false)
    private Instant dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanState state = LoanState.PENDING_APPROVAL;

    @OneToMany(mappedBy = "loan", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<LoanInstallment> installment;

    @OneToMany(mappedBy = "loan")
    private List<LoanRepaymentReceipt> repayments;
}
