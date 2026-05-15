package com.appointment.appointment_service.dto;

import jakarta.validation.constraints.NotNull;

public class AppointmentRequest {

    @NotNull
    private Long patientId;

    @NotNull
    private Long providerId;

    @NotNull
    private Long slotId;

    @NotNull
    private String serviceType;

    private String notes;

    @NotNull
    private String modeOfConsultation;

    public AppointmentRequest() {
    }

    public AppointmentRequest(Long patientId, Long providerId, Long slotId, String serviceType, String notes, String modeOfConsultation) {
        this.patientId = patientId;
        this.providerId = providerId;
        this.slotId = slotId;
        this.serviceType = serviceType;
        this.notes = notes;
        this.modeOfConsultation = modeOfConsultation;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getModeOfConsultation() {
        return modeOfConsultation;
    }

    public void setModeOfConsultation(String modeOfConsultation) {
        this.modeOfConsultation = modeOfConsultation;
    }
}