package com.ezra.lending_app.api.controllers;

import com.ezra.lending_app.api.dto.loan.LoanRepaymentRequestDto;
import com.ezra.lending_app.api.dto.loan.LoanRepaymentResponseDto;
import com.ezra.lending_app.api.dto.loan.LoanRequestDto;
import com.ezra.lending_app.api.dto.loan.LoanResponseDto;
import com.ezra.lending_app.domain.enums.LoanState;
import com.ezra.lending_app.domain.services.LoanRepaymentService;
import com.ezra.lending_app.domain.services.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RequestMapping("/loans")
@RestController
public class LoanController {

    public final LoanService loanService;
    public final LoanRepaymentService loanRepaymentService;

    @GetMapping("/products/{productCode}/customers/{customerCode}")
    public LoanResponseDto checkLoanEligibility(@PathVariable String productCode, @PathVariable String customerCode) {
        return loanService.checkLoanEligibility(productCode, customerCode);
    }

    @PostMapping("/products/{productCode}/customers/{customerCode}")
    public LoanResponseDto applyForLoan(@PathVariable String productCode,
                                        @PathVariable String customerCode,
                                        @Valid @RequestBody LoanRequestDto loanRequestDto) {
        return loanService.applyForLoan(productCode, customerCode, loanRequestDto);
    }

    @GetMapping("/{loanCode}")
    public LoanResponseDto getLoanDetails(@PathVariable String loanCode) {
        return loanService.getLoanDetails(loanCode);
    }

    @PutMapping("/{loanCode}/workflow")
    public LoanResponseDto updateLoanStatus(@PathVariable String loanCode, @RequestParam LoanState status) {
        return loanService.loanWorkflow(loanCode, status);
    }

    @PostMapping("/{loanCode}/repay")
    public LoanRepaymentResponseDto repayLoan(@PathVariable String loanCode,
                                              @Valid @RequestBody LoanRepaymentRequestDto loanRepaymentRequest) {
        return loanRepaymentService.repayLoan(loanCode, loanRepaymentRequest);
    }

    @GetMapping("/{loanCode}/repayments")
    public List<LoanRepaymentResponseDto> getLoanRepaymentReceipts(@PathVariable String loanCode) {
        return loanRepaymentService.getLoanRepaymentReceipts(loanCode);
    }
}
