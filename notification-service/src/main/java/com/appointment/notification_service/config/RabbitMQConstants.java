package com.appointment.notification_service.config;

public class RabbitMQConstants {

    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.routingKey";

    private RabbitMQConstants() {
    }
}
