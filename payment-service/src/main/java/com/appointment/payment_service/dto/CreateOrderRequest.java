package com.appointment.payment_service.dto;

import jakarta.validation.constraints.NotNull;

public class CreateOrderRequest {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long patientId;

    @NotNull
    private Integer amount; // paise

    private String currency;
    private String notes;

    public CreateOrderRequest() {
    }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
