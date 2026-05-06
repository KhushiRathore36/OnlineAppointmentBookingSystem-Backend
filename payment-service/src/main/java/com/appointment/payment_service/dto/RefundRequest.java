package com.appointment.payment_service.dto;



import jakarta.validation.constraints.NotNull;

public class RefundRequest {

    @NotNull
    private Long appointmentId;

    private String notes;

    public RefundRequest() {
    }

    public RefundRequest(Long appointmentId, String notes) {
        this.appointmentId = appointmentId;
        this.notes = notes;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}