package com.appointment.auth_service.dto;

public class ChangePasswordRequest {

    private String oldPassword;
    private String newPassword;

    // Default Constructor
    public ChangePasswordRequest() {}

    // Getters & Setters

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
