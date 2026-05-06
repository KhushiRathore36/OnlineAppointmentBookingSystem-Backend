package com.appointment.notification_service.service;



import com.appointment.notification_service.dto.BulkNotificationRequest;
import com.appointment.notification_service.dto.NotificationRequest;
import com.appointment.notification_service.entity.Notification;

import java.util.List;

public interface NotificationService {

    Notification send(NotificationRequest request);

    List<Notification> sendBulk(BulkNotificationRequest request);

    Notification markAsRead(Long notificationId);

    List<Notification> markAllRead(Long recipientId);

    List<Notification> getByRecipient(Long recipientId);

    long getUnreadCount(Long recipientId);

    void deleteNotification(Long notificationId);

    void sendEmail(Notification notification);

    void sendSMS(Notification notification);

    List<Notification> getAll();
}
