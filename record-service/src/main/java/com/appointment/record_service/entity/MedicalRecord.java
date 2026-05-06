package com.appointment.record_service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records", uniqueConstraints = {
        @UniqueConstraint(columnNames = "appointmentId")
})
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @Column(nullable = false, unique = true)
    private Long appointmentId;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long providerId;

    @Column(nullable = false, length = 500)
    private String diagnosis;

    @Column(nullable = false, length = 1000)
    private String prescription;

    @Column(nullable = false, length = 2000)
    private String notes;

    private String attachmentUrl;

    private LocalDate followUpDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public MedicalRecord() {
    }

    public MedicalRecord(Long recordId, Long appointmentId, Long patientId, Long providerId,
                         String diagnosis, String prescription, String notes,
                         String attachmentUrl, LocalDate followUpDate,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.recordId = recordId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.providerId = providerId;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
        this.attachmentUrl = attachmentUrl;
        this.followUpDate = followUpDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
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

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public LocalDate getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
