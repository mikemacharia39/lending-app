package com.ezra.lending_app.domain.services;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.ezra.lending_app.api.dto.notification.NotificationDto;
import com.ezra.lending_app.domain.entities.Customer;
import com.ezra.lending_app.domain.entities.Loan;
import com.ezra.lending_app.domain.entities.NotificationTemplate;
import com.ezra.lending_app.domain.enums.LoanState;
import com.ezra.lending_app.domain.enums.NotificationChannel;
import com.ezra.lending_app.domain.enums.NotificationType;
import com.ezra.lending_app.domain.repositories.NotificationTemplateRepository;
import com.ezra.lending_app.domain.services.notification.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class LoanNotificationService {
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final Map<NotificationChannel, INotificationService> notificationChannelServices;

    @Transactional(readOnly = true)
    public void sendLoanStateChangeNotification(final Loan loan) {
        CompletableFuture.runAsync(() -> sendNotification(loan));
    }

    private void sendNotification(final Loan loan) {
        NotificationType notificationType = getNotificationTypeForLoanState(loan.getState());
        NotificationTemplate template = notificationTemplateRepository.findByNotificationType(notificationType)
                .orElseThrow(() -> new IllegalStateException("Notification template not found for type: " + notificationType));

        NotificationDto notification = NotificationDto.builder()
                .template(template.getMessageTemplate())
                .recipientData(NotificationDto.RecipientData.builder()
                        .customerName(loan.getCustomer().getFirstName())
                        .amount(loan.getRequestedAmount().toString())
                        .loanDueDate(loan.getDueDate().toString())
                        .build())
                .build();

        String message = formulateMessage(template.getMessageTemplate(), notification);
        notification.setMessage(message);
        notification.setRecipient(getNotificationMedia(loan.getCustomer()));

        NotificationChannel preferredChannel = loan.getCustomer().getPreferredNotificationChannel();
        INotificationService notificationService = notificationChannelServices.get(preferredChannel);
        if (notificationService != null) {
            notificationService.sendNotification(notification);
        } else {
            notificationChannelServices.get(NotificationChannel.SMS).sendNotification(notification);
        }
    }

    private NotificationType getNotificationTypeForLoanState(LoanState loanState) {
        return switch (loanState) {
            case OPEN -> NotificationType.LOAN_APPROVAL;
            case CLOSED -> NotificationType.LOAN_COMPLETED;
            case REJECTED -> NotificationType.LOAN_REJECTION;
            case OVERDUE -> NotificationType.PAST_DUE;
            case WRITTEN_OFF -> NotificationType.WRITTEN_OFF;
            default -> throw new IllegalArgumentException("Unsupported loan state: " + loanState);
        };
    }

    private String formulateMessage(String template, NotificationDto notificationDto) {
        return template.replace("{{customerName}}", notificationDto.getRecipientData().getCustomerName())
                .replace("{{amount}}", notificationDto.getRecipientData().getAmount())
                .replace("{{loanReference}}", notificationDto.getRecipientData().getLoanReference())
                .replace("{{loanDueDate}}", notificationDto.getRecipientData().getLoanDueDate());
    }

    private String getNotificationMedia(Customer customer) {
        return switch (customer.getPreferredNotificationChannel()) {
            case EMAIL -> customer.getEmail();
            case PUSH_NOTIFICATION -> customer.getDeviceId();
            default -> customer.getPhoneNumber();
        };
    }
}