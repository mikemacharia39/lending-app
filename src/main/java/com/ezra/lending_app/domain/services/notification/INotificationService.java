package com.ezra.lending_app.domain.services.notification;

import com.ezra.lending_app.api.dto.notification.NotificationDto;

public interface INotificationService {
    // support sending notification via email, sms and push notification
    // define a method that will be overridden by the implementing class
    void sendNotification(NotificationDto notification);
}
