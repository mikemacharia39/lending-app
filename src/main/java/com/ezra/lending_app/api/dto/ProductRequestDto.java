package com.ezra.lending_app.api.dto;

import java.math.BigDecimal;
import java.util.List;

import com.ezra.lending_app.domain.enums.LoanTerm;
import com.ezra.lending_app.domain.enums.ProductStatus;
import com.ezra.lending_app.domain.enums.RepaymentFrequencyType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductRequestDto {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 1000)
    private String description;
    @NotNull
    @NotBlank
    private String currency;
    @NotNull
    @Min(value = 1, message = "Minimum loan amount must be greater than 0")
    private BigDecimal maxLoanAmount;
    @NotNull
    @Min(value = 1, message = "Minimum loan amount must be greater than 0")
    private int minLoanTermDuration;
    @NotNull
    private LoanTerm minLoanTermType;
    @NotNull
    @Min(value = 1, message = "Maximum loan term duration must be greater than 0")
    private int maxLoanTermDuration;
    @NotNull
    private LoanTerm maxLoanTermType;
    private List<ProductFeeDto> fees;
    @NotNull
    private ProductStatus status;
    @NotNull
    private RepaymentFrequencyType repaymentFrequency;
    @NotNull
    private int gracePeriodAfterLoanDueDateInDays;
}
