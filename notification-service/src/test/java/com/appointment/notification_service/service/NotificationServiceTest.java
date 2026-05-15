package com.appointment.notification_service.service;

import com.appointment.notification_service.dto.NotificationMessage;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Test
    void testNotificationProcessing() {

        NotificationService notificationService = mock(NotificationService.class);

        NotificationConsumer notificationConsumer =
                new NotificationConsumer(notificationService);

        NotificationMessage message = new NotificationMessage(
                1L,
                "test@gmail.com",
                "Appointment Booked",
                "Your appointment is confirmed",
                "BOOKING"
        );

        notificationConsumer.consumeNotification(message);

        verify(notificationService, times(1)).send(any());
    }
}