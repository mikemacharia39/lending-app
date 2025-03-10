package com.ezra.lending_app.domain.services.notification;

import com.ezra.lending_app.api.dto.notification.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotificationService implements INotificationService {
    @Override
    public void sendNotification(NotificationDto notification) {
        log.info("Sending email notification to recipient {}", notification.getRecipient());
        log.info("The message is {}", notification.getMessage());
    }
}