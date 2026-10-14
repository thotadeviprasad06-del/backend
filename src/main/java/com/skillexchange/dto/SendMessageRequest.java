package com.skillexchange.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for sending a message in an exchange conversation.
 */
public class SendMessageRequest {

    @NotBlank(message = "Message content cannot be blank")
    @Size(max = 2000, message = "Message must not exceed 2000 characters")
    private String content;

    public SendMessageRequest() {
    }

    public SendMessageRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
