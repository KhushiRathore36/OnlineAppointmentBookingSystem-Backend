package com.appointment.record_service.dto;



import jakarta.validation.constraints.NotBlank;

public class AttachmentRequest {

    @NotBlank(message = "Attachment URL is required")
    private String attachmentUrl;

    public AttachmentRequest() {
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
}