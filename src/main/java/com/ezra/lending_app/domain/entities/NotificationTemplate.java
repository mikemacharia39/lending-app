package com.ezra.lending_app.domain.entities;

import com.ezra.lending_app.domain.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import static com.ezra.lending_app.domain.util.RandomReferenceGenerator.generateReference;

@Entity
@Table(name = "notification_template")
public class NotificationTemplate extends BaseEntity {
    final String code = generateReference();

    @Column(name = "template_name", nullable = false)
    private String templateName;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Column(nullable = false)
    private String messageTemplate;
}
