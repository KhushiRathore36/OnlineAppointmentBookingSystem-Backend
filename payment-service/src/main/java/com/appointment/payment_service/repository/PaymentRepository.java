package com.appointment.payment_service.repository;

import com.appointment.payment_service.entity.Payment;
import com.appointment.payment_service.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByAppointmentId(Long appointmentId);

    List<Payment> findByPatientId(Long patientId);

    List<Payment> findByStatus(PaymentStatus status);

    Optional<Payment> findByTransactionId(String transactionId);
    
    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);
}