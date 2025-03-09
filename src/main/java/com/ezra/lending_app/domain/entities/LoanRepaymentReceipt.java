package com.ezra.lending_app.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

import static com.ezra.lending_app.domain.util.RandomReferenceGenerator.generateReference;

@Entity
@Table(name = "loan_repayment")
public class LoanRepaymentReceipt extends BaseEntity {
    @Column(nullable = false, unique = true)
    private final String code = generateReference();

    @ManyToOne
    private Loan loan;

    @Column(name = "payment_channel", nullable = false)
    private String paymentChannel;

    @Column(name = "payment_reference", nullable = false)
    private String paymentReference;

    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;
}
