package com.appointment.notification_service.service;

import com.appointment.notification_service.dto.NotificationMessage;
import com.appointment.notification_service.dto.NotificationRequest;
import com.appointment.notification_service.entity.NotificationChannel;
import com.appointment.notification_service.entity.NotificationType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "notification.queue")
    public void consumeNotification(NotificationMessage notificationMessage) {

        System.out.println("Message received from queue:");
        System.out.println("Type: " + notificationMessage.getType());
        System.out.println("Recipient: " + notificationMessage.getRecipient());

        NotificationRequest request = new NotificationRequest();
        request.setRecipientId(notificationMessage.getUserId());
        request.setRecipientEmail(notificationMessage.getRecipient());
        request.setTitle(notificationMessage.getSubject());
        request.setMessage(notificationMessage.getMessage());
        request.setChannel(NotificationChannel.EMAIL);
        request.setType(mapToEnum(notificationMessage.getType()));

        notificationService.send(request);
    }

    private NotificationType mapToEnum(String type) {
        if (type == null) return NotificationType.BOOKING;

        switch (type.toUpperCase()) {
            case "BOOKING":
            case "APPOINTMENT_BOOKED":
                return NotificationType.BOOKING;
            case "REMINDER":
                return NotificationType.REMINDER;
            case "CANCELLATION":
            case "CANCEL":
            case "APPOINTMENT_CANCELLED":
                return NotificationType.CANCELLATION;
            case "COMPLETED":
            case "APPOINTMENT_COMPLETED":
                return NotificationType.COMPLETED;
            case "RESCHEDULE":
            case "RESCHEDULED":
            case "APPOINTMENT_RESCHEDULED":
                return NotificationType.RESCHEDULE;
            case "PAYMENT":
                return NotificationType.PAYMENT;
            case "FOLLOWUP":
                return NotificationType.FOLLOWUP;
            default:
                System.out.println("Unknown notification type: " + type + ", defaulting to BOOKING");
                return NotificationType.BOOKING;
        }
    }
}