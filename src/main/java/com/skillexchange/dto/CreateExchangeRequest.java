package com.skillexchange.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for initiating a skill exchange proposal.
 */
public class CreateExchangeRequest {

    @NotNull(message = "Receiver user ID is required")
    private Long receiverId;

    @NotNull(message = "Offered skill ID is required")
    private Long offeredSkillId; // What the requester will teach

    @NotNull(message = "Wanted skill ID is required")
    private Long wantedSkillId; // What the requester wants to learn

    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;

    public CreateExchangeRequest() {
    }

    public CreateExchangeRequest(Long receiverId, Long offeredSkillId, Long wantedSkillId, String message) {
        this.receiverId = receiverId;
        this.offeredSkillId = offeredSkillId;
        this.wantedSkillId = wantedSkillId;
        this.message = message;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getOfferedSkillId() {
        return offeredSkillId;
    }

    public void setOfferedSkillId(Long offeredSkillId) {
        this.offeredSkillId = offeredSkillId;
    }

    public Long getWantedSkillId() {
        return wantedSkillId;
    }

    public void setWantedSkillId(Long wantedSkillId) {
        this.wantedSkillId = wantedSkillId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
