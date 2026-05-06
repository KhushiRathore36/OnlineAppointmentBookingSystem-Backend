package com.appointment.schedule_service.entity;


import jakarta.persistence.*;
import java.time.*;

@Entity
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    private Long providerId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int durationMinutes;

    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    private String recurrence; // DAILY / WEEKLY / NONE
    private LocalDateTime createdAt;

    // ===== GETTERS =====

    public Long getSlotId() {
        return slotId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ===== SETTERS =====

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
