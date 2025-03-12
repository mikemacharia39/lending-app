package com.ezra.lending_app.domain.services;

import java.util.List;

import com.ezra.lending_app.api.dto.loan.LoanRepaymentRequestDto;
import com.ezra.lending_app.api.dto.loan.LoanRepaymentResponseDto;
import com.ezra.lending_app.domain.entities.Loan;
import com.ezra.lending_app.domain.entities.LoanRepaymentReceipt;
import com.ezra.lending_app.domain.enums.LoanState;
import com.ezra.lending_app.domain.enums.NotificationType;
import com.ezra.lending_app.domain.mappers.loan.LoanRepaymentMapper;
import com.ezra.lending_app.domain.repositories.LoanRepaymentReceiptRepository;
import com.ezra.lending_app.domain.repositories.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@RequiredArgsConstructor
@Service
public class LoanRepaymentService {

    private final LoanRepaymentReceiptRepository loanRepaymentReceiptRepository;
    private final LoanRepository loanRepository;
    private final LoanRepaymentMapper loanRepaymentMapper;
    private final LoanService loanService;
    private final LoanNotificationService loanNotificationService;

    public LoanRepaymentResponseDto repayLoan(final String loanCode, final LoanRepaymentRequestDto loanRepaymentRequest) {

        List<LoanRepaymentReceipt> existingPaymentReceipts =
                loanRepaymentReceiptRepository.findAllByPaymentReferenceAndPaymentChannel(
                        loanRepaymentRequest.paymentReference(),
                        loanRepaymentRequest.paymentChannel());

        if (!existingPaymentReceipts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment already made");
        }
        Loan loan = loanService.getLoanEntity(loanCode);

        LoanRepaymentReceipt loanRepaymentReceipt = loanRepaymentMapper.toEntity(loanRepaymentRequest);
        loanRepaymentReceipt.setLoan(loan);
        loanRepaymentReceiptRepository.save(loanRepaymentReceipt);

        loan.setRepaidAmount(loan.getRepaidAmount().add(loanRepaymentRequest.amount()));

        if (loan.getRepaidAmount().compareTo(loan.getFullLoanAmountPlusFees()) >= 0) {
            loan.setState(LoanState.CLOSED);
            loanRepository.save(loan);
            loanNotificationService.sendLoanNotification(loan, NotificationType.LOAN_COMPLETED);
        } else {
            loanNotificationService.sendLoanNotification(loan, NotificationType.SUCCESS_PAYMENT);
        }

        return loanRepaymentMapper.toDto(loanRepaymentReceipt);
    }

    public List<LoanRepaymentResponseDto> getLoanRepaymentReceipts(final String loanCode) {
        List<LoanRepaymentReceipt> loanRepaymentsForLoan = loanRepaymentReceiptRepository.findAllByLoanCode(loanCode);

        return loanRepaymentsForLoan.stream()
                .map(loanRepaymentMapper::toDto)
                .toList();
    }
}
