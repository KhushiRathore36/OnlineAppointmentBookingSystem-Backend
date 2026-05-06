package com.appointment.payment_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Long appointmentId;
    private Long patientId;
    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMode mode;

    @Column(unique = true)
    private String transactionId;

    private String currency;
    private LocalDateTime paidAt;
    private LocalDateTime refundedAt;

    @Column(length = 1000)
    private String notes;

    private String gateway;
    private String gatewayOrderId;
    private String gatewayPaymentId;
    private String gatewaySignature;

    public Payment() {
    }

    public Payment(Long paymentId, Long appointmentId, Long patientId, Double amount,
                   PaymentStatus status, PaymentMode mode, String transactionId,
                   String currency, LocalDateTime paidAt, LocalDateTime refundedAt,
                   String notes, String gateway, String gatewayOrderId,
                   String gatewayPaymentId, String gatewaySignature) {
        this.paymentId = paymentId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.amount = amount;
        this.status = status;
        this.mode = mode;
        this.transactionId = transactionId;
        this.currency = currency;
        this.paidAt = paidAt;
        this.refundedAt = refundedAt;
        this.notes = notes;
        this.gateway = gateway;
        this.gatewayOrderId = gatewayOrderId;
        this.gatewayPaymentId = gatewayPaymentId;
        this.gatewaySignature = gatewaySignature;
    }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public PaymentMode getMode() { return mode; }
    public void setMode(PaymentMode mode) { this.mode = mode; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public LocalDateTime getRefundedAt() { return refundedAt; }
    public void setRefundedAt(LocalDateTime refundedAt) { this.refundedAt = refundedAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getGateway() { return gateway; }
    public void setGateway(String gateway) { this.gateway = gateway; }

    public String getGatewayOrderId() { return gatewayOrderId; }
    public void setGatewayOrderId(String gatewayOrderId) { this.gatewayOrderId = gatewayOrderId; }

    public String getGatewayPaymentId() { return gatewayPaymentId; }
    public void setGatewayPaymentId(String gatewayPaymentId) { this.gatewayPaymentId = gatewayPaymentId; }

    public String getGatewaySignature() { return gatewaySignature; }
    public void setGatewaySignature(String gatewaySignature) { this.gatewaySignature = gatewaySignature; }
}