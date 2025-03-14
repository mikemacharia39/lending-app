package com.ezra.lending_app.domain.services.notification;

import com.ezra.lending_app.api.dto.notification.NotificationDto;

public interface INotificationService {
    void sendNotification(NotificationDto notification);
}
