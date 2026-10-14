package com.skillexchange.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO providing platform-wide statistics for the Administrator Dashboard.
 */
public class AdminDashboardDto {

    private long totalUsers;
    private long activeUsers;
    private long totalSkills;
    private long pendingRequests;
    private long acceptedExchanges;
    private long completedExchanges;
    private long totalMessages;

    private List<ExchangeResponseDto> recentSystemExchanges = new ArrayList<>();

    public AdminDashboardDto() {
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(long activeUsers) {
        this.activeUsers = activeUsers;
    }

    public long getTotalSkills() {
        return totalSkills;
    }

    public void setTotalSkills(long totalSkills) {
        this.totalSkills = totalSkills;
    }

    public long getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(long pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public long getAcceptedExchanges() {
        return acceptedExchanges;
    }

    public void setAcceptedExchanges(long acceptedExchanges) {
        this.acceptedExchanges = acceptedExchanges;
    }

    public long getCompletedExchanges() {
        return completedExchanges;
    }

    public void setCompletedExchanges(long completedExchanges) {
        this.completedExchanges = completedExchanges;
    }

    public long getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(long totalMessages) {
        this.totalMessages = totalMessages;
    }

    public List<ExchangeResponseDto> getRecentSystemExchanges() {
        return recentSystemExchanges;
    }

    public void setRecentSystemExchanges(List<ExchangeResponseDto> recentSystemExchanges) {
        this.recentSystemExchanges = recentSystemExchanges;
    }
}
