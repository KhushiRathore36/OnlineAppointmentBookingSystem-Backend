package com.appointment.notification_service.controller;


import com.appointment.notification_service.dto.NotificationMessage;
import com.appointment.notification_service.service.NotificationProducer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-notifications")
public class NotificationTestController {

    private final NotificationProducer notificationProducer;

    public NotificationTestController(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    @PostMapping
    public String sendTestNotification(@RequestBody NotificationMessage notificationMessage) {
        notificationProducer.sendNotification(notificationMessage);
        return "Notification message sent to RabbitMQ successfully";
    }
}
