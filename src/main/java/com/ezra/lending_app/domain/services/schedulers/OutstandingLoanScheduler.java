package com.ezra.lending_app.domain.services.schedulers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.ezra.lending_app.domain.entities.Loan;
import com.ezra.lending_app.domain.entities.Product;
import com.ezra.lending_app.domain.entities.ProductFee;
import com.ezra.lending_app.domain.enums.FeeType;
import com.ezra.lending_app.domain.enums.LoanState;
import com.ezra.lending_app.domain.enums.NotificationType;
import com.ezra.lending_app.domain.repositories.LoanRepository;
import com.ezra.lending_app.domain.services.LoanNotificationService;
import com.ezra.lending_app.domain.services.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class OutstandingLoanScheduler {
    private final LoanRepository loanRepository;
    private final LoanService loanService;
    private final LoanNotificationService loanNotificationService;

    /**
     * 1. For each loan get the loan due date and
     * 2. Then compare with the product gracePeriodBeforeLoanDueDateInDays and gracePeriodAfterLoanDueDateInDays
     * 3. If the difference in days between the loan due date and the current date is between 0 and gracePeriodBeforeLoanDueDateInDays then end notification informing user of loan due
     * 4. If the due date is reached, the send a specific notification informing user to pay before the date as passed
     * 5. If the due date is passed and the difference in days between the loan due date and the current date is between 0 and gracePeriodAfterLoanDueDateInDays then end notification informing user of loan overdue
     * 5.1. if past the gracePeriodAfterLoanDueDateInDays then change the loan state to OVERDUE and apply late fees
     * <p>
     * [0 0 6 * * ?] run every day at 6 am
     * [0 * * ? * *] run every minute - for demo
     *
     */
    @Scheduled(cron = "0 * * ? * *")
    @Transactional
    public void processOutstandingLoans() {
        log.info("Started outstanding loan scheduler...");
        List<Loan> loans = loanRepository.findAllByStateIn(List.of(LoanState.OVERDUE, LoanState.OPEN));
        loans.forEach(this::processLoan);
        log.info("Completed processing outstanding loans");
    }

    private void processLoan(Loan loan) {
        log.info("Processing loan with code {}", loan.getCode());
        Instant now = Instant.now();
        long daysUntilDueDate = ChronoUnit.DAYS.between(now, loan.getDueDate());
        long daysPastDueDate = ChronoUnit.DAYS.between(loan.getDueDate(), now);

        // if repaid amount is greater than or equal to the full loan amount plus fees, then close the loan
        if (loan.getRepaidAmount().compareTo(loan.getFullLoanAmountPlusFees()) >= 0) {
            loan.transitionState(LoanState.CLOSED);
            loanRepository.save(loan);
            loanNotificationService.sendLoanNotification(loan, NotificationType.LOAN_COMPLETED);
            return;
        }
        if (daysUntilDueDate >= 0 && daysUntilDueDate <= loan.getProduct().getGracePeriodBeforeLoanDueDateInDays()) {
            loanNotificationService.sendLoanNotification(loan, NotificationType.LOAN_BEFORE_DUE);
        } else if (daysUntilDueDate == 0) {
            loanNotificationService.sendLoanNotification(loan, NotificationType.LOAN_DUE, assessLateFee(loan));
        } else if (daysPastDueDate >= 0 && daysPastDueDate <= loan.getProduct().getGracePeriodAfterLoanDueDateInDays()) {
            loanNotificationService.sendLoanNotification(loan, NotificationType.PAST_DUE, assessLateFee(loan));
        } else if (daysPastDueDate > loan.getProduct().getGracePeriodAfterLoanDueDateInDays()) {
            loan.transitionState(LoanState.OVERDUE);
            loanService.applyLateFees(loan);
            loanNotificationService.sendLoanNotification(loan, NotificationType.PAST_DUE, assessLateFee(loan));
        }
    }

    private BigDecimal assessLateFee(final Loan loan) {
        Product product = loan.getProduct();
        ProductFee lateRunningFee = product.getFees().stream()
                .filter(productFee -> productFee.getFeeType() == FeeType.LATE_FEE)
                .findFirst()
                .orElse(null);
        if (lateRunningFee != null) {
            return loanService.getAmountFromFee(lateRunningFee, loan.getRequestedAmount());
        }
        return BigDecimal.ZERO;
    }
}
