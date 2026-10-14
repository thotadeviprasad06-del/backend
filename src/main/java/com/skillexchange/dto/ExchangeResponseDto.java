package com.skillexchange.dto;

import java.time.LocalDateTime;

/**
 * DTO returned for skill exchange queries, preventing circular references and entity leaks.
 */
public class ExchangeResponseDto {

    private Long id;
    private Long requesterId;
    private String requesterName;
    private String requesterEmail;

    private Long receiverId;
    private String receiverName;
    private String receiverEmail;

    private SkillDto offeredSkill;
    private SkillDto wantedSkill;

    private String message;
    private String status; // PENDING, ACCEPTED, REJECTED, CANCELLED, COMPLETED

    private Long conversationId; // available when ACCEPTED or COMPLETED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ExchangeResponseDto() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public SkillDto getOfferedSkill() {
        return offeredSkill;
    }

    public void setOfferedSkill(SkillDto offeredSkill) {
        this.offeredSkill = offeredSkill;
    }

    public SkillDto getWantedSkill() {
        return wantedSkill;
    }

    public void setWantedSkill(SkillDto wantedSkill) {
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

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
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
