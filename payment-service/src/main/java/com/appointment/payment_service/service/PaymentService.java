package com.appointment.payment_service.service;


import com.appointment.payment_service.dto.CreateOrderRequest;
import com.appointment.payment_service.dto.CreateOrderResponse;
import com.appointment.payment_service.dto.PaymentRequest;
import com.appointment.payment_service.dto.VerifyPaymentRequest;
import com.appointment.payment_service.entity.Payment;

import java.util.List;

public interface PaymentService {

    Payment processPayment(PaymentRequest request);

    Payment getPaymentByAppointment(Long appointmentId);

    List<Payment> getPaymentsByPatient(Long patientId);

    List<Payment> getPaymentHistory();

    Payment refundPayment(Long appointmentId, String notes);

    String getPaymentStatus(Long appointmentId);

    Payment updatePaymentStatus(Long paymentId, String status);

    String generateInvoice(Long appointmentId);

    Double getTotalRevenue();
    CreateOrderResponse createOrder(CreateOrderRequest request);
    Payment verifyPayment(VerifyPaymentRequest request);
}