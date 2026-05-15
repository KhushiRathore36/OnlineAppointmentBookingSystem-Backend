package com.appointment.payment_service.controller;

import com.appointment.payment_service.dto.CreateOrderRequest;
import com.appointment.payment_service.dto.CreateOrderResponse;
import com.appointment.payment_service.dto.PaymentRequest;
import com.appointment.payment_service.dto.RefundRequest;
import com.appointment.payment_service.dto.StatusUpdateRequest;
import com.appointment.payment_service.dto.VerifyPaymentRequest;
import com.appointment.payment_service.entity.Payment;
import com.appointment.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Payment processPayment(@Valid @RequestBody PaymentRequest request) {
        return paymentService.processPayment(request);
    }

    @PostMapping("/create-order")
    public CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return paymentService.createOrder(request);
    }

    @PostMapping("/verify")
    public Payment verifyPayment(@Valid @RequestBody VerifyPaymentRequest request) {
        return paymentService.verifyPayment(request);
    }

    @GetMapping("/appointment/{appointmentId}")
    public Payment getPaymentByAppointment(@PathVariable Long appointmentId) {
        return paymentService.getPaymentByAppointment(appointmentId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Payment> getPaymentsByPatient(@PathVariable Long patientId) {
        return paymentService.getPaymentsByPatient(patientId);
    }

    @GetMapping("/history")
    public List<Payment> getPaymentHistory() {
        return paymentService.getPaymentHistory();
    }

    @PostMapping("/refund")
    public Payment refundPayment(@Valid @RequestBody RefundRequest request) {
        return paymentService.refundPayment(request.getAppointmentId(), request.getNotes());
    }

    @GetMapping("/status/{appointmentId}")
    public String getPaymentStatus(@PathVariable Long appointmentId) {
        return paymentService.getPaymentStatus(appointmentId);
    }

    @PutMapping("/{paymentId}/status")
    public Payment updatePaymentStatus(@PathVariable Long paymentId,
                                       @Valid @RequestBody StatusUpdateRequest request) {
        return paymentService.updatePaymentStatus(paymentId, request.getStatus());
    }

    @GetMapping("/invoice/{appointmentId}")
    public String generateInvoice(@PathVariable Long appointmentId) {
        return paymentService.generateInvoice(appointmentId);
    }

    @GetMapping("/revenue")
    public Double getTotalRevenue() {
        return paymentService.getTotalRevenue();
    }
}