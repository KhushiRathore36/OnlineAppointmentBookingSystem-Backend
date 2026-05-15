package com.appointment.notification_service.service;

import com.appointment.notification_service.config.RabbitMQConstants;
import com.appointment.notification_service.dto.NotificationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(NotificationMessage notificationMessage) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.NOTIFICATION_EXCHANGE,
                RabbitMQConstants.NOTIFICATION_ROUTING_KEY,
                notificationMessage
        );
    }
}

