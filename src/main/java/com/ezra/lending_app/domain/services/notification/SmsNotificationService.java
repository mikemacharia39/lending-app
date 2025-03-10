package com.ezra.lending_app.domain.services.notification;

import com.ezra.lending_app.api.dto.notification.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsNotificationService implements INotificationService {
    @Override
    public void sendNotification(NotificationDto notification) {
        log.info("Sending SMS notification...");
        log.info("Sending message {} to recipient {}", notification.getMessage(), notification.getRecipient());
    }
}
