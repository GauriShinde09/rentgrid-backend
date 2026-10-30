package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SupportRequestDto {

    @NotBlank(message = "Subject is required")
    @Size(max = 150, message = "Subject cannot exceed 150 characters")
    private String subject;

    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    private String message;

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
}