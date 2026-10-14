package com.skillexchange.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO providing metrics and recent activities for the User Dashboard.
 */
public class UserDashboardDto {

    private String userFullName;
    private long skillsOfferedCount;
    private long skillsWantedCount;
    private long incomingRequestsCount;
    private long outgoingRequestsCount;
    private long pendingExchangesCount;
    private long acceptedExchangesCount;
    private long completedExchangesCount;
    private long unreadNotificationsCount;
    private long unreadMessagesCount;

    private List<ExchangeResponseDto> recentExchanges = new ArrayList<>();
    private List<UserMatchDto> topRecommendations = new ArrayList<>();

    public UserDashboardDto() {
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public long getSkillsOfferedCount() {
        return skillsOfferedCount;
    }

    public void setSkillsOfferedCount(long skillsOfferedCount) {
        this.skillsOfferedCount = skillsOfferedCount;
    }

    public long getSkillsWantedCount() {
        return skillsWantedCount;
    }

    public void setSkillsWantedCount(long skillsWantedCount) {
        this.skillsWantedCount = skillsWantedCount;
    }

    public long getIncomingRequestsCount() {
        return incomingRequestsCount;
    }

    public void setIncomingRequestsCount(long incomingRequestsCount) {
        this.incomingRequestsCount = incomingRequestsCount;
    }

    public long getOutgoingRequestsCount() {
        return outgoingRequestsCount;
    }

    public void setOutgoingRequestsCount(long outgoingRequestsCount) {
        this.outgoingRequestsCount = outgoingRequestsCount;
    }

    public long getPendingExchangesCount() {
        return pendingExchangesCount;
    }

    public void setPendingExchangesCount(long pendingExchangesCount) {
        this.pendingExchangesCount = pendingExchangesCount;
    }

    public long getAcceptedExchangesCount() {
        return acceptedExchangesCount;
    }

    public void setAcceptedExchangesCount(long acceptedExchangesCount) {
        this.acceptedExchangesCount = acceptedExchangesCount;
    }

    public long getCompletedExchangesCount() {
        return completedExchangesCount;
    }

    public void setCompletedExchangesCount(long completedExchangesCount) {
        this.completedExchangesCount = completedExchangesCount;
    }

    public long getUnreadNotificationsCount() {
        return unreadNotificationsCount;
    }

    public void setUnreadNotificationsCount(long unreadNotificationsCount) {
        this.unreadNotificationsCount = unreadNotificationsCount;
    }

    public long getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(long unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    public List<ExchangeResponseDto> getRecentExchanges() {
        return recentExchanges;
    }

    public void setRecentExchanges(List<ExchangeResponseDto> recentExchanges) {
        this.recentExchanges = recentExchanges;
    }

    public List<UserMatchDto> getTopRecommendations() {
        return topRecommendations;
    }

    public void setTopRecommendations(List<UserMatchDto> topRecommendations) {
        this.topRecommendations = topRecommendations;
    }
}
