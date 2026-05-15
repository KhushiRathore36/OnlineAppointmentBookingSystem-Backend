package com.appointment.payment_service.dto;


import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {

    @NotNull
    private String status;

    public StatusUpdateRequest() {
    }

    public StatusUpdateRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
