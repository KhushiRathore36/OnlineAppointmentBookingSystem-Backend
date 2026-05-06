package com.appointment.payment_service.dto;



import jakarta.validation.constraints.NotNull;

public class PaymentRequest {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long patientId;

    @NotNull
    private Double amount;

    @NotNull
    private String mode;

    private String currency;
    private String notes;

    public PaymentRequest() {
    }

    public PaymentRequest(Long appointmentId, Long patientId, Double amount,
                          String mode, String currency, String notes) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.amount = amount;
        this.mode = mode;
        this.currency = currency;
        this.notes = notes;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}