package com.ezra.lending_app.domain.entities;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static com.ezra.lending_app.domain.util.RandomReferenceGenerator.generateReference;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "loan_repayment")
public class LoanRepaymentReceipt extends BaseEntity {
    @Builder.Default
    @Column(nullable = false, unique = true)
    private final String code = generateReference();

    @JoinColumn(name = "loan_id", nullable = false)
    @ManyToOne
    private Loan loan;

    private BigDecimal amount;

    @Column(name = "payment_channel", nullable = false)
    private String paymentChannel;

    @Column(name = "payment_reference", nullable = false)
    private String paymentReference;

    @Column(name = "payment_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Instant paymentDate;
}
