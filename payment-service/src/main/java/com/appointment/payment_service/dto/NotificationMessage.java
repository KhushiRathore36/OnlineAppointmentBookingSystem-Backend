package com.appointment.payment_service.dto;



import java.io.Serializable;

public class NotificationMessage implements Serializable {

    private Long userId;
    private String recipient;
    private String subject;
    private String message;
    private String type;

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

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }
}