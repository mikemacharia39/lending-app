package com.ezra.lending_app.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static com.ezra.lending_app.domain.util.RandomReferenceGenerator.generateReference;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "loan_repayment")
public class LoanRepaymentReceipt extends BaseEntity {
    @Column(nullable = false, unique = true)
    private final String code = generateReference();

    @JoinColumn(name = "loan_id", nullable = false)
    @ManyToOne
    private Loan loan;

    @Column(name = "payment_channel", nullable = false)
    private String paymentChannel;

    @Column(name = "payment_reference", nullable = false)
    private String paymentReference;

    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;
}
