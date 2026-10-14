package com.skillexchange.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity managing a formal skill barter exchange proposal between two users.
 * Supports status transitions: PENDING -> ACCEPTED / REJECTED / CANCELLED -> COMPLETED.
 * Maps to the 'exchange_requests' table in MySQL.
 */
@Entity
@Table(name = "exchange_requests")
public class ExchangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "offered_skill_id", nullable = false)
    private Skill offeredSkill; // What the requester will teach

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "wanted_skill_id", nullable = false)
    private Skill wantedSkill; // What the requester wants to learn

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false, length = 30)
    private String status = "PENDING"; // PENDING, ACCEPTED, REJECTED, CANCELLED, COMPLETED

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ExchangeRequest() {
    }

    public ExchangeRequest(User requester, User receiver, Skill offeredSkill, Skill wantedSkill, String message) {
        this.requester = requester;
        this.receiver = receiver;
        this.offeredSkill = offeredSkill;
        this.wantedSkill = wantedSkill;
        this.message = message;
        this.status = "PENDING";
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Skill getOfferedSkill() {
        return offeredSkill;
    }

    public void setOfferedSkill(Skill offeredSkill) {
        this.offeredSkill = offeredSkill;
    }

    public Skill getWantedSkill() {
        return wantedSkill;
    }

    public void setWantedSkill(Skill wantedSkill) {
        this.wantedSkill = wantedSkill;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
