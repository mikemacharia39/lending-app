package com.ezra.lending_app.domain.repositories;

import com.ezra.lending_app.domain.entities.NotificationTemplate;
import com.ezra.lending_app.domain.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Optional<NotificationTemplate> findByNotificationType(NotificationType notificationType);
}
