package com.appointment.appointment_service.service;


import com.appointment.appointment_service.config.RabbitMQConstants;
import com.appointment.appointment_service.dto.NotificationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(NotificationMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.NOTIFICATION_EXCHANGE,
                RabbitMQConstants.NOTIFICATION_ROUTING_KEY,
                message
        );
    }
}