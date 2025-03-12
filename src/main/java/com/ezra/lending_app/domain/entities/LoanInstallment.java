package com.ezra.lending_app.domain.entities;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.ezra.lending_app.domain.util.RandomReferenceGenerator.generateReference;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "loan_installment")
public class LoanInstallment extends BaseEntity {
    @Builder.Default
    @Column(nullable = false, unique = true)
    private final String code = generateReference();

    @ManyToOne
    private Loan loan;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "due_date", nullable = false)
    private Instant dueDate;
}
