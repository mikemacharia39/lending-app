package com.ezra.lending_app.domain.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class LoanNotificationService {
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final Map<NotificationChannel, INotificationService> notificationChannelServices;

    @Transactional(readOnly = true)
    @Async
    public void sendLoanNotification(final Loan loan, final NotificationType notificationType) {
        NotificationDto notification = getNotificationDto(loan, notificationType);

        String message = formulateMessage(notification.getTemplate(), notification, notificationType);
        notification.setMessage(message);
        notification.setRecipient(getNotificationMedia(loan.getCustomer()));

        notifyCustomer(loan.getCustomer(), notification);
    }

    @Transactional(readOnly = true)
    @Async
    public void sendLoanStateChangeNotification(final Loan loan) {
        NotificationType notificationType = getNotificationTypeForLoanState(loan.getState());

        NotificationDto notification = getNotificationDto(loan, notificationType);

        String message = formulateMessage(notification.getTemplate(), notification, notificationType);
        notification.setMessage(message);
        notification.setRecipient(getNotificationMedia(loan.getCustomer()));

        notifyCustomer(loan.getCustomer(), notification);
    }

    private NotificationDto getNotificationDto(Loan loan, NotificationType notificationType) {
        NotificationTemplate template = notificationTemplateRepository.findByNotificationType(notificationType)
                .orElseThrow(() -> new IllegalStateException("Notification template not found for type: " + notificationType));

        return NotificationDto.builder()
                .template(template.getMessageTemplate())
                .recipientData(NotificationDto.RecipientData.builder()
                        .customerName(loan.getCustomer().getFirstName())
                        .amount(loan.getRequestedAmount().toString())
                        .loanDueDate(loan.getDueDate().toString())
                        .loanReference(loan.getCode())
                        .productName(loan.getProduct().getName())
                        .paymentDate(Instant.now().toString())
                        .daysOverdue(String.valueOf(Duration.between(loan.getDueDate(), Instant.now()).toDays()))
                        .daysUntilDue(String.valueOf(Duration.between(Instant.now(), loan.getDueDate()).toDays()))
                        .build())
                .build();
    }

    private void notifyCustomer(Customer customer, NotificationDto notification) {
        NotificationChannel preferredChannel = customer.getPreferredNotificationChannel();
        INotificationService notificationService = notificationChannelServices.get(preferredChannel);
        if (notificationService != null) {
            notificationService.sendNotification(notification);
        } else {
            notificationChannelServices.get(NotificationChannel.SMS).sendNotification(notification);
        }
    }

    private NotificationType getNotificationTypeForLoanState(LoanState loanState) {
        return switch (loanState) {
            case OPEN -> NotificationType.LOAN_APPROVED;
            case CLOSED -> NotificationType.LOAN_COMPLETED;
            case REJECTED -> NotificationType.LOAN_REJECTION;
            case OVERDUE -> NotificationType.PAST_DUE;
            case WRITTEN_OFF -> NotificationType.WRITTEN_OFF;
            case PENDING_APPROVAL -> NotificationType.LOAN_PENDING_APPROVAL;
            default -> throw new IllegalArgumentException("Unsupported loan state: " + loanState);
        };
    }

    private String formulateMessage(String template, NotificationDto notificationDto, NotificationType notificationType) {
        String message = template.replace("{{customerName}}", notificationDto.getRecipientData().getCustomerName())
                .replace("{{amount}}", String.format("%s %s", notificationDto.getRecipientData().getCurrency(), notificationDto.getRecipientData().getAmount()))
                .replace("{{loanReference}}", notificationDto.getRecipientData().getLoanReference())
                .replace("{{loanDueDate}}", notificationDto.getRecipientData().getLoanDueDate());

        switch (notificationType) {
            case SUCCESS_PAYMENT:
                message = message.replace("{{paymentDate}}", notificationDto.getRecipientData().getPaymentDate());
                break;
            case LOAN_APPROVED:
                message = message.replace("{{productName}}", notificationDto.getRecipientData().getProductName());
                break;
            case PAST_DUE:
                message = message.replace("{{daysOverdue}}", notificationDto.getRecipientData().getDaysOverdue());
                break;
            case LOAN_DUE:
                message = message.replace("{{lateFeeAmount}}", notificationDto.getRecipientData().getLateFeeAmount());
                break;
            case LOAN_BEFORE_DUE:
                message = message.replace("{{daysUntilDue}}", notificationDto.getRecipientData().getDaysUntilDue());
                break;
            default:
                break;
        }

        return message;
    }

    private String getNotificationMedia(Customer customer) {
        return switch (customer.getPreferredNotificationChannel()) {
            case EMAIL -> customer.getEmail();
            case PUSH_NOTIFICATION -> customer.getDeviceId();
            default -> customer.getPhoneNumber();
        };
    }
}