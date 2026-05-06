package com.appointment.notification_service.repository;



import com.appointment.notification_service.entity.Notification;
import com.appointment.notification_service.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientIdOrderBySentAtDesc(Long recipientId);

    List<Notification> findByRecipientIdAndIsReadOrderBySentAtDesc(Long recipientId, boolean isRead);

    long countByRecipientIdAndIsRead(Long recipientId, boolean isRead);

    List<Notification> findByType(NotificationType type);

    List<Notification> findByRelatedId(Long relatedId);

    void deleteByNotificationId(Long notificationId);
}
