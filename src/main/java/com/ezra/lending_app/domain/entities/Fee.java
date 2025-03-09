package com.ezra.lending_app.domain.entities;

import com.ezra.lending_app.domain.enums.FeeType;
import com.ezra.lending_app.domain.enums.LoanFeeValueType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "fee")
public class Fee {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "fee_type", nullable = false)
    private FeeType feeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type", nullable = false)
    private LoanFeeValueType valueType;

    private BigDecimal value;

    @Column(name = "applied_at_origination", nullable = false)
    private boolean appliedAtOrigination;

    @Column(name = "daily_accrual", nullable = false)
    private boolean dailyAccrual;
}
