package com.ezra.lending_app.domain.services;

import com.ezra.lending_app.api.dto.loan.LoanFeeDto;
import com.ezra.lending_app.api.dto.loan.LoanInstallmentDto;
import com.ezra.lending_app.api.dto.loan.LoanRequestDto;
import com.ezra.lending_app.api.dto.loan.LoanResponseDto;
import com.ezra.lending_app.domain.entities.Customer;
import com.ezra.lending_app.domain.entities.Loan;
import com.ezra.lending_app.domain.entities.LoanFee;
import com.ezra.lending_app.domain.entities.LoanInstallment;
import com.ezra.lending_app.domain.entities.LoanRepaymentReceipt;
import com.ezra.lending_app.domain.entities.Product;
import com.ezra.lending_app.domain.entities.ProductFee;
import com.ezra.lending_app.domain.enums.FeeType;
import com.ezra.lending_app.domain.enums.LoanState;
import com.ezra.lending_app.domain.enums.ProductLoanTenure;
import com.ezra.lending_app.domain.enums.RepaymentFrequencyType;
import com.ezra.lending_app.domain.mappers.loan.LoanMapper;
import com.ezra.lending_app.domain.repositories.LoanRepaymentReceiptRepository;
import com.ezra.lending_app.domain.repositories.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final ProductService productService;
    private final CustomerService customerService;
    private final ValidationService validationService;
    private final LoanNotificationService loanNotificationService;
    private final LoanRepaymentReceiptRepository loanRepaymentReceiptRepository;

    public LoanResponseDto checkLoanEligibility(String productCode, String customerCode) {
        Customer customer = customerService.getCustomerEntity(customerCode);
        Product product = productService.getProductEntity(productCode);

        if (!product.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This product is no longer available");
        }

        // Fetch the customer's repayment history
        List<Loan> loanHistory = loanRepository.findAllByCustomerCode(customerCode);

        // Get the most recent loan amount
        BigDecimal lastLoanAmount = BigDecimal.ZERO;
        if (!loanHistory.isEmpty()) {
            lastLoanAmount = loanHistory.stream()
                    .sorted((loan1, loan2) -> loan2.getDisbursedDate().compareTo(loan1.getDisbursedDate()))
                    .map(Loan::getRequestedAmount)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);
        }

        // Calculate creditworthiness based on repayment punctuality
        BigDecimal creditworthiness = calculateCreditworthiness(loanHistory);

        // Determine the loan amount based on product limits and creditworthiness
        BigDecimal loanAmount = determineLoanAmount(product, lastLoanAmount, creditworthiness);

        // Ensure the loan amount is within the product's min and max loan amounts
        if (loanAmount.compareTo(product.getMinLoanAmount()) < 0 || loanAmount.compareTo(product.getMaxLoanAmount()) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The calculated loan amount is outside the allowed range for this product");
        }

        final List<LoanFeeDto> loanFees = calculateLoanFees(product.getFees(), loanAmount);
        final BigDecimal fullLoanAmount = loanAmount.add(loanFees.stream()
                .map(LoanFeeDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return LoanResponseDto.builder()
                .customerCode(customerCode)
                .productCode(productCode)
                .requestedAmount(loanAmount)
                .disbursedAmount(loanAmount)
                .fullLoanAmountPlusFees(fullLoanAmount)
                .repaidAmount(BigDecimal.ZERO)
                .loanFees(loanFees)
                .installment(calculateLoanInstallments(product, fullLoanAmount))
                .build();
    }

    /**
     * Apply for a loan by checking the loan eligibility and saving the loan details.
     * If the customer's loan request is lesser than the amount they are eligible for, the loan amount will be auto approved
     * If greater, the loan amount will be sent for manual approval
     *
     * @param productCode  product code
     * @param customerCode customer code
     * @param loanRequest  loan request details
     * @return loan results
     */
    public LoanResponseDto applyForLoan(final String productCode, final String customerCode, final LoanRequestDto loanRequest) {
        LoanResponseDto loanEligibility = checkLoanEligibility(productCode, customerCode);
        Customer customer = customerService.getCustomerEntity(customerCode);
        Product product = productService.getProductEntity(productCode);

        BigDecimal requestedAmount = loanRequest.requestedAmount();
        List<LoanFeeDto> loanFees = calculateLoanFees(product.getFees(), requestedAmount);
        BigDecimal fullLoanAmountPlusFees = requestedAmount.add(loanFees.stream()
                .map(LoanFeeDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        Instant disbursedDate = Instant.now();
        Instant dueDate = disbursedDate.plus(Duration.ofDays(product.getMaxLoanTermDuration()));

        BigDecimal disbursedAmount = requestedAmount; // Assuming disbursed amount is the same as requested amount initially
        // if there is a service and the fee applies at origination, the disbursed amount will be the requested amount minus the service fee
        // that also means the full loan amount will be reduced if fee is applied at origination
        if (!loanFees.isEmpty()) {
            ProductFee serviceFee = product.getFees().stream()
                    .filter(fee -> fee.getFeeType() == FeeType.SERVICE_FEE && fee.isAppliedAtOrigination())
                    .findFirst()
                    .orElse(null);
            if (serviceFee != null) {
                BigDecimal serviceFeeAmount = getAmountFromFee(serviceFee, requestedAmount);
                disbursedAmount = requestedAmount.subtract(serviceFeeAmount);
                fullLoanAmountPlusFees = fullLoanAmountPlusFees.subtract(serviceFeeAmount);
            }
        }

        // if the loan requested by a customer is less than the amount they are eligible for and it's a flexible loan, auto approve the loan
        LoanState loanState = requestedAmount.compareTo(loanEligibility.getRequestedAmount()) <= 0
                    && product.getLoanTenure() == ProductLoanTenure.FLEXIBLE_TENURE
                ? LoanState.OPEN
                : LoanState.PENDING_APPROVAL;

        Loan loan = Loan.builder()
                .product(product)
                .customer(customer)
                .requestedAmount(requestedAmount)
                .disbursedAmount(disbursedAmount)
                .fullLoanAmountPlusFees(fullLoanAmountPlusFees)
                .repaidAmount(BigDecimal.ZERO)
                .loanTerm(loanRequest.loanTerm())
                .loanPeriod(loanRequest.loanPeriod())
                .disbursedDate(disbursedDate)
                .dueDate(dueDate)
                .state(loanState)
                .build();

        loan.setInstallment(loanInstallmentDtoToEntity(calculateLoanInstallments(product, fullLoanAmountPlusFees), loan));
        loan.setLoanFees(loanFeeDtoToEntity(loanFees, loan));
        loanRepository.save(loan);

        loanNotificationService.sendLoanStateChangeNotification(loan);

        return loanMapper.dto(loan);
    }

    public LoanResponseDto loanWorkflow(final String loanCode, final LoanState newLoanState) {
        Loan loan = loanRepository.findByLoanCode(loanCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found"));

        validationService.validateLoanStateTransition(loan.getState(), newLoanState);
        loan.transitionState(newLoanState);
        loanRepository.save(loan);

        loanNotificationService.sendLoanStateChangeNotification(loan);

        return loanMapper.dto(loan);
    }

    private List<LoanInstallment> loanInstallmentDtoToEntity(List<LoanInstallmentDto> loanInstallments, Loan loan) {
        return loanInstallments.stream()
                .map(loanInstallmentDto -> LoanInstallment.builder()
                        .loan(loan)
                        .amount(loanInstallmentDto.getAmount())
                        .dueDate(loanInstallmentDto.getDueDate())
                        .build())
                .toList();
    }

    private List<LoanFee> loanFeeDtoToEntity(List<LoanFeeDto> loanFees, Loan loan) {
        return loanFees.stream()
                .map(loanFeeDto -> LoanFee.builder()
                        .loan(loan)
                        .feeType(loanFeeDto.getFeeType())
                        .amount(loanFeeDto.getAmount())
                        .appliedDate(loanFeeDto.getAppliedDate())
                        .build())
                .toList();
    }

    /**
     * Calculate creditworthiness based on repayment punctuality. The creditworthiness is assessed by evaluating
     * the percentage of on-time payments, overdue payments, and written-off loans.
     * Weights are assigned to each category to determine the final creditworthiness value.
     *
     * @param loanHistory List of loans for the customer
     * @return Creditworthiness value
     */
    private BigDecimal calculateCreditworthiness(List<Loan> loanHistory) {
        // Implement logic to calculate creditworthiness based on repayment punctuality
        // For example, you can calculate the percentage of on-time payments
        long successfullyPaidLoans = 0;
        long overdueLoans = 0;
        long writtenOffLoans = 0;
        long allLoans = loanHistory.size();
        for (Loan loan : loanHistory) {
            if (loan.getState() == LoanState.CLOSED) {
                successfullyPaidLoans++;
            }
            if (loan.getState() == LoanState.WRITTEN_OFF) {
                writtenOffLoans++;
            }
            // a loan can be closed but was paid late, compare the loan due date with the last receipt payment date
            if (loan.getState() == LoanState.CLOSED) {
                List<LoanRepaymentReceipt> receipts = loanRepaymentReceiptRepository.findAllByLoanId(loan.getId());
                if (!receipts.isEmpty()) {
                    // check if any of the receipt payment was made after the loan due date
                    boolean isPaidLate = receipts.stream()
                            .anyMatch(receipt -> receipt.getPaymentDate().isAfter(loan.getDueDate()));
                    if (isPaidLate) {
                        overdueLoans++;
                    }
                }
            }
        }

        final BigDecimal successfullyPaidRatio = BigDecimal.valueOf(successfullyPaidLoans).divide(BigDecimal.valueOf(allLoans), 2, RoundingMode.HALF_EVEN);
        final BigDecimal overdueRatio = BigDecimal.valueOf(overdueLoans).divide(BigDecimal.valueOf(allLoans), 2, RoundingMode.HALF_EVEN);
        final BigDecimal writtenOffRatio = BigDecimal.valueOf(writtenOffLoans).divide(BigDecimal.valueOf(allLoans), 2, RoundingMode.HALF_EVEN);

        // Suggested weights for each category
        final BigDecimal weightSuccessfullyPaid = BigDecimal.valueOf(0.6);
        final BigDecimal weightOverdue = BigDecimal.valueOf(0.3);
        final BigDecimal weightWrittenOff = BigDecimal.valueOf(0.1);

        // if creditworthiness is negative, the loan amount will be reduced, if positive it will be increased
        return successfullyPaidRatio.multiply(weightSuccessfullyPaid)
                .subtract(overdueRatio.multiply(weightOverdue))
                .subtract(writtenOffRatio.multiply(weightWrittenOff));
    }

    /**
     * Determine the loan amount based on product limits and creditworthiness.
     *
     * @param product          The product for which the loan is being processed.
     * @param lastLoanAmount   Last loan amount
     * @param creditworthiness assessment of creditworthiness
     * @return new loan amount
     */
    private BigDecimal determineLoanAmount(Product product, BigDecimal lastLoanAmount, BigDecimal creditworthiness) {
        BigDecimal minLoanAmount = product.getMinLoanAmount();
        BigDecimal maxLoanAmount = product.getMaxLoanAmount();

        if (lastLoanAmount.compareTo(BigDecimal.ZERO) == 0) {
            return minLoanAmount;
        } else {
            BigDecimal newLoanAmount = lastLoanAmount.add(lastLoanAmount.multiply(creditworthiness));
            if (newLoanAmount.compareTo(minLoanAmount) < 0) {
                return minLoanAmount;
            } else if (newLoanAmount.compareTo(maxLoanAmount) > 0) {
                return maxLoanAmount;
            } else {
                return newLoanAmount;
            }
        }
    }

    public List<LoanResponseDto> getCustomerLoan(String customerCode) {
        List<Loan> loans = loanRepository.findAllByCustomerCode(customerCode);
        return loans.stream()
                .map(loanMapper::dto)
                .toList();
    }

    public List<LoanInstallmentDto> calculateLoanInstallments(Product product, BigDecimal loanAmount) {
        List<LoanInstallmentDto> installments = new ArrayList<>();

        ProductLoanTenure loanTenure = product.getLoanTenure();
        RepaymentFrequencyType repaymentFrequency = product.getRepaymentFrequency();
        if (loanTenure == ProductLoanTenure.FLEXIBLE_TENURE) {
            LoanInstallmentDto installment = LoanInstallmentDto.builder()
                    .amount(loanAmount)
                    .dueDate(Instant.now().plus(Duration.ofDays(product.getMaxLoanTermDuration())))
                    .build();
            installments.add(installment);
        }
        return installments;
    }

    /**
     * Assess loan fees based on the product fees and requested amount.
     *
     * @param productFees     List of product fees
     * @param requestedAmount Requested loan amount
     * @return the evaluated loan amount from the product fees
     */
    public List<LoanFeeDto> calculateLoanFees(List<ProductFee> productFees, BigDecimal requestedAmount) {
        List<LoanFeeDto> loanFees = new ArrayList<>();
        for (ProductFee productFee : productFees) {
            BigDecimal amount = getAmountFromFee(productFee, requestedAmount);
            LoanFeeDto loanFeeDto = LoanFeeDto.builder()
                    .feeType(productFee.getFeeType())
                    .amount(amount)
                    .appliedDate(Instant.now())
                    .build();
            loanFees.add(loanFeeDto);
        }
        return loanFees;
    }

    public BigDecimal getAmountFromFee(final ProductFee productFee, final BigDecimal requestedAmount) {
        BigDecimal amount = BigDecimal.ZERO;
        switch (productFee.getValueType()) {
            case PERCENTAGE:
                amount = requestedAmount.multiply(productFee.getValue());
                break;
            case FIXED_AMOUNT:
                amount = productFee.getValue();
                break;
            default:
                break;
        }
        return amount;
    }
}
