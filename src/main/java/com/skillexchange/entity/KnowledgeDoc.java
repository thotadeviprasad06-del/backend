package com.skillexchange.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity storing knowledge base documents, guides, and FAQs
 * used by the grounded Retrieval-Augmented Generation (RAG) assistant.
 */
@Entity
@Table(name = "knowledge_docs")
public class KnowledgeDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 80)
    private String topic;

    @Column(nullable = false, length = 50)
    private String category; // "GETTING_STARTED", "SKILLS", "EXCHANGE_WORKFLOW", "COMMUNICATION", "RULES"

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 255)
    private String keywords;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public KnowledgeDoc() {
    }

    public KnowledgeDoc(String title, String topic, String category, String content, String keywords) {
        this.title = title;
        this.topic = topic;
        this.category = category;
        this.content = content;
        this.keywords = keywords;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
