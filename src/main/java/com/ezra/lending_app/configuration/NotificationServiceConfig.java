package com.ezra.lending_app.configuration;

import com.ezra.lending_app.domain.enums.NotificationChannel;
import com.ezra.lending_app.domain.services.notification.EmailNotificationService;
import com.ezra.lending_app.domain.services.notification.INotificationService;
import com.ezra.lending_app.domain.services.notification.PushNotificationService;
import com.ezra.lending_app.domain.services.notification.SmsNotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class NotificationServiceConfig {

    @Bean
    public Map<NotificationChannel, INotificationService> notificationChannelServices(
            SmsNotificationService smsNotificationService,
            EmailNotificationService emailNotificationService,
            PushNotificationService pushNotificationService) {
        Map<NotificationChannel, INotificationService> services = new EnumMap<>(NotificationChannel.class);
        services.put(NotificationChannel.SMS, smsNotificationService);
        services.put(NotificationChannel.EMAIL, emailNotificationService);
        services.put(NotificationChannel.PUSH_NOTIFICATION, pushNotificationService);
        return services;
    }
}
