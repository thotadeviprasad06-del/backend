package com.skillexchange.dto;

import java.time.LocalDateTime;

/**
 * DTO for conversation overview.
 */
public class ConversationDto {

    private Long id;
    private Long exchangeRequestId;
    private String exchangeStatus;

    private Long partnerId;
    private String partnerName;
    private String partnerEmail;

    private String offeredSkillName;
    private String wantedSkillName;

    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private long unreadCount;

    public ConversationDto() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExchangeRequestId() {
        return exchangeRequestId;
    }

    public void setExchangeRequestId(Long exchangeRequestId) {
        this.exchangeRequestId = exchangeRequestId;
    }

    public String getExchangeStatus() {
        return exchangeStatus;
    }

    public void setExchangeStatus(String exchangeStatus) {
        this.exchangeStatus = exchangeStatus;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerEmail() {
        return partnerEmail;
    }

    public void setPartnerEmail(String partnerEmail) {
        this.partnerEmail = partnerEmail;
    }

    public String getOfferedSkillName() {
        return offeredSkillName;
    }

    public void setOfferedSkillName(String offeredSkillName) {
        this.offeredSkillName = offeredSkillName;
    }

    public String getWantedSkillName() {
        return wantedSkillName;
    }

    public void setWantedSkillName(String wantedSkillName) {
        this.wantedSkillName = wantedSkillName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }
}
