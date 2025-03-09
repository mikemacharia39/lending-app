package com.ezra.lending_app.api.dto.loan;

import com.ezra.lending_app.domain.enums.FeeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoanFeeDto {
    private FeeType feeType;
    private BigDecimal amount;
    private Instant appliedDate;
}
