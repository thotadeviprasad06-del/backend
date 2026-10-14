package com.skillexchange.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for sending a question to the grounded RAG AI Assistant.
 */
public class AiChatRequest {

    @NotBlank(message = "Question cannot be blank")
    @Size(max = 500, message = "Question must not exceed 500 characters")
    private String question;

    public AiChatRequest() {
    }

    public AiChatRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
