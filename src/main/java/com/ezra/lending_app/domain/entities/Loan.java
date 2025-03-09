package com.ezra.lending_app.domain.entities;

import com.ezra.lending_app.domain.enums.LoanState;
import com.ezra.lending_app.domain.enums.LoanTerm;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    @Column(name = "product_id", nullable = false)
    private Long productId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_term_type", nullable = false)
    private LoanTerm loanTermType;

    private int termDuration;

    private Instant disbursedDate;
    private Instant dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanState state = LoanState.PENDING_APPROVAL;

    @OneToMany(mappedBy = "loan")
    private List<LoanInstallment> installment;

    @OneToMany(mappedBy = "loan")
    private List<LoanRepaymentReceipt> repayments;
}
