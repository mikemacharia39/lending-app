package com.ezra.lending_app.api.dto;

import com.ezra.lending_app.domain.enums.FeeType;
import com.ezra.lending_app.domain.enums.LoanFeeValueType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record FeeDto(
        @NotBlank
        @NotNull
        @Size(min = 1, max = 255)
        String name,
        @NotNull
        FeeType feeType,
        @NotNull
        LoanFeeValueType valueType,
        @NotNull
        @Min(0)
        BigDecimal value,
        @NotNull
        boolean appliedAtOrigination,
        @NotNull
        boolean dailyAccrual
) {
}
