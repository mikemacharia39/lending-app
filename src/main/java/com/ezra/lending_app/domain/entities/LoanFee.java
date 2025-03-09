package com.ezra.lending_app.domain.entities;

import com.ezra.lending_app.domain.enums.FeeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "loan_fee")
public class LoanFee {
    @JoinColumn(name = "loan_id", nullable = false)
    @ManyToOne
    private Loan loan;

    @Enumerated(EnumType.STRING)
    @Column(name = "fee_type", nullable = false)
    private FeeType feeType;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "applied_date", nullable = false)
    private Instant appliedDate;
}
