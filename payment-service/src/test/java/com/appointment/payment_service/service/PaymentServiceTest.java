package com.appointment.payment_service.service;

import com.appointment.payment_service.client.UserClient;
import com.appointment.payment_service.dto.PaymentRequest;
import com.appointment.payment_service.entity.Payment;
import com.appointment.payment_service.entity.PaymentMode;
import com.appointment.payment_service.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private NotificationProducer notificationProducer;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void testProcessPayment_Success() {

        PaymentRequest request = new PaymentRequest();
        request.setAppointmentId(1L);
        request.setPatientId(101L);
        request.setAmount(500.0);
        request.setMode("CASH");

        when(paymentRepository.findByAppointmentId(1L))
                .thenReturn(Optional.empty());

        Payment savedPayment = new Payment();
        savedPayment.setPatientId(101L);

        when(paymentRepository.save(Mockito.any(Payment.class)))
                .thenReturn(savedPayment);

        Map<String, Object> user = new HashMap<>();
        user.put("email", "test@gmail.com");

        when(userClient.getUserById(101L)).thenReturn(user);

        Payment result = paymentService.processPayment(request);

        assertNotNull(result);
        verify(notificationProducer, times(1)).sendNotification(Mockito.any());
    }
}