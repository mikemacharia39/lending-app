package com.ezra.lending_app.domain.entities;

import com.ezra.lending_app.domain.enums.FeeType;
import com.ezra.lending_app.domain.enums.LoanFeeValueType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "fee")
public class ProductFee extends BaseEntity {
    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne
    private Product product;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "fee_type", nullable = false)
    private FeeType feeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type", nullable = false)
    private LoanFeeValueType valueType;

    @Column(name = "fee_value", nullable = false)
    private BigDecimal value;

    @Column(name = "applied_at_origination", nullable = false)
    private boolean appliedAtOrigination;

    @Column(name = "daily_accrual", nullable = false)
    private boolean dailyAccrual;
}
