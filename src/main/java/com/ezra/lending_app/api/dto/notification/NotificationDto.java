package com.ezra.lending_app.api.dto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Builder
@Setter
public class NotificationDto {
    private String recipient;
    private String template;
    private String message;
    private RecipientData recipientData;

    // sample loan notification template message
    // payment received: Dear {{customerName}}, thank you for your payment of {{amount}} on {{paymentDate}}. Your payment has been successfully received and updated in your account.
    // Approved: Dear {{customerName}}, your {{productName}} loan {{loanReference}} of {{amount}} has been issued. Please repay your loan by {{loanDueDate}}.
    // Overdue: Dear {{customerName}}, your loan payment of {{amount}} is now {{daysOverdue}} days overdue. Please make the payment immediately to avoid further late fees and penalties.
    // due: Dear {{customerName}}}, your loan of {{amount}} is due today. A late fee of {{lateFeeAmount}} will apply if not paid on time.
    // before due: Dear {{customerName}}, this is a reminder that your loan payment of {{amount}} is due in {{daysUntilDue}} days. Please make the payment on time to avoid late fees.
    // rejected: Dear {{customerName}}, your loan application {{loanReference}} worth {{amount}} has been rejected. Please contact us for more information.
    // loan completed: Dear {{customerName}}, your loan {{loanReference}} has been fully repaid. Thank you for your business.

    @Getter
    @Builder
    public static class RecipientData {
        private String customerName;
        private String currency;
        private BigDecimal amount;
        private String paymentDate;
        private String productName;
        private String loanDueDate;
        private String daysOverdue;
        private BigDecimal lateFeeAmount;
        private String daysUntilDue;
        private String loanReference;
    }
}
