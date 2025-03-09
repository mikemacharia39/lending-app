package com.ezra.lending_app.api.dto;

import lombok.Builder;

@Builder
public class NotificationDto {
    private String recipient;
    private String template;
    private String message;

    // sample loan notification template message
    // success: Dear {{customerName}}, your loan application has been approved.
    // Approved: Dear {{customerName}}, your {{productName}} loan of {{amount}} has been approved. Your [Repayment Tenure]-month repayment period starts on [Loan Start Date]. Call us at [Contact Number] for more information.
    // Overdue: Dear {{customerName}}, your payment is overdue. A late fee will appear on your next bill. Please contact us on [phone number] if you have issues.
    // due: Dear {{customerName}}}, your loan of {{Amount}} is due today {{DayMonthYear}}. A late fee of {{late_fee_amount}} will apply if not paid on time.
}
