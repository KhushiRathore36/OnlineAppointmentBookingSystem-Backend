package com.appointment.appointment_service.dto;



import jakarta.validation.constraints.NotNull;

public class RescheduleRequest {

    @NotNull
    private Long newSlotId;

    public RescheduleRequest() {
    }

    public RescheduleRequest(Long newSlotId) {
        this.newSlotId = newSlotId;
    }

    public Long getNewSlotId() {
        return newSlotId;
    }

    public void setNewSlotId(Long newSlotId) {
        this.newSlotId = newSlotId;
    }
}
