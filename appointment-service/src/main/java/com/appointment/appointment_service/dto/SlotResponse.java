package com.appointment.appointment_service.dto;



import java.time.LocalDate;
import java.time.LocalTime;

public class SlotResponse {

    private Long slotId;
    private Long providerId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int durationMinutes;
    private String status;
    private String recurrence;

    public SlotResponse() {
    }

    public SlotResponse(Long slotId, Long providerId, LocalDate date, LocalTime startTime,
                        LocalTime endTime, int durationMinutes, String status, String recurrence) {
        this.slotId = slotId;
        this.providerId = providerId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.status = status;
        this.recurrence = recurrence;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }
}