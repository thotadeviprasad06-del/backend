package com.skillexchange.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO returned by the grounded RAG AI Assistant.
 * Grounded in retrieved platform documentation and guides.
 */
public class AiChatResponse {

    private String question;
    private String answer;
    private List<String> retrievedSources = new ArrayList<>();
    private double confidenceScore;

    public AiChatResponse() {
    }

    public AiChatResponse(String question, String answer, List<String> retrievedSources, double confidenceScore) {
        this.question = question;
        this.answer = answer;
        this.retrievedSources = retrievedSources;
        this.confidenceScore = confidenceScore;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getRetrievedSources() {
        return retrievedSources;
    }

    public void setRetrievedSources(List<String> retrievedSources) {
        this.retrievedSources = retrievedSources;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
}
