package com.appointment.notification_service.dto;

import java.io.Serializable;

public class NotificationMessage implements Serializable {

    private Long userId;
    private String recipient; // doctor email
    private String subject;
    private String message;
    private String type; // APPOINTMENT_CANCELLED etc.
    private Long relatedId;
    private String relatedType;

    public NotificationMessage() {
    }

    public NotificationMessage(Long userId, String recipient, String subject, String message, String type) {
        this.userId = userId;
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }
}