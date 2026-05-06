package com.appointment.notification_service.service;

import com.appointment.notification_service.dto.BulkNotificationRequest;
import com.appointment.notification_service.dto.NotificationRequest;
import com.appointment.notification_service.entity.Notification;
import com.appointment.notification_service.entity.NotificationChannel;
import com.appointment.notification_service.exception.ResourceNotFoundException;
import com.appointment.notification_service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification send(NotificationRequest request) {

        Notification notification = new Notification();
        notification.setRecipientId(request.getRecipientId());
        notification.setRecipientEmail(request.getRecipientEmail());
        notification.setType(request.getType());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setChannel(request.getChannel());
        notification.setRelatedId(request.getRelatedId());
        notification.setRelatedType(request.getRelatedType());
        notification.setRead(false);
        notification.setSentAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);

        if (saved.getChannel() == NotificationChannel.EMAIL) {
            sendEmail(saved);
        } else if (saved.getChannel() == NotificationChannel.SMS) {
            sendSMS(saved);
        }

        return saved;
    }

    @Override
    public List<Notification> sendBulk(BulkNotificationRequest request) {

        List<Notification> savedNotifications = new ArrayList<>();

        for (Long recipientId : request.getRecipientIds()) {

            Notification notification = new Notification();
            notification.setRecipientId(recipientId);

            // Bulk request mein agar email list nahi hai, toh yeh null rahega
            notification.setRecipientEmail(null);

            notification.setType(request.getType());
            notification.setTitle(request.getTitle());
            notification.setMessage(request.getMessage());
            notification.setChannel(request.getChannel());
            notification.setRelatedId(request.getRelatedId());
            notification.setRelatedType(request.getRelatedType());
            notification.setRead(false);
            notification.setSentAt(LocalDateTime.now());

            Notification saved = notificationRepository.save(notification);

            if (saved.getChannel() == NotificationChannel.EMAIL) {
                sendEmail(saved);
            } else if (saved.getChannel() == NotificationChannel.SMS) {
                sendSMS(saved);
            }

            savedNotifications.add(saved);
        }

        return savedNotifications;
    }

    @Override
    public Notification markAsRead(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id: " + notificationId
                ));

        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> markAllRead(Long recipientId) {

        List<Notification> notifications =
                notificationRepository.findByRecipientIdAndIsReadOrderBySentAtDesc(
                        recipientId,
                        false
                );

        for (Notification notification : notifications) {
            notification.setRead(true);
        }

        return notificationRepository.saveAll(notifications);
    }

    @Override
    public List<Notification> getByRecipient(Long recipientId) {
        return notificationRepository.findByRecipientIdOrderBySentAtDesc(recipientId);
    }

    @Override
    public long getUnreadCount(Long recipientId) {
        return notificationRepository.countByRecipientIdAndIsRead(recipientId, false);
    }

    @Override
    public void deleteNotification(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id: " + notificationId
                ));

        notificationRepository.delete(notification);
    }

    @Override
    public void sendEmail(Notification notification) {

        if (mailSender == null) {
            System.out.println("Mail sender not configured. Skipping email for: "
                    + notification.getTitle());
            return;
        }

        if (notification.getRecipientEmail() == null ||
                notification.getRecipientEmail().isBlank()) {

            System.out.println("Recipient email missing. Skipping email for: "
                    + notification.getTitle());
            return;
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(notification.getRecipientEmail());
            mailMessage.setSubject(notification.getTitle());
            mailMessage.setText(notification.getMessage());

            mailSender.send(mailMessage);

            System.out.println("Email sent successfully to: "
                    + notification.getRecipientEmail());

        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }
    }

    @Override
    public void sendSMS(Notification notification) {

        System.out.println("SMS sent to recipientId " + notification.getRecipientId()
                + " | Title: " + notification.getTitle()
                + " | Message: " + notification.getMessage());
    }

    @Override
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }
}