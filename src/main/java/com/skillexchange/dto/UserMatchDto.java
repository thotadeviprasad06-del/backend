package com.skillexchange.dto;

/**
 * DTO representing an algorithmic match recommendation.
 * Shows why two users complement each other's educational goals.
 */
public class UserMatchDto {

    private UserProfileDto user;
    private int matchScore; // 0 to 100
    private SkillDto matchedOfferedSkill; // What they can teach you
    private SkillDto matchedWantedSkill;  // What you can teach them
    private String matchReason;

    public UserMatchDto() {
    }

    public UserMatchDto(UserProfileDto user, int matchScore, SkillDto matchedOfferedSkill, SkillDto matchedWantedSkill, String matchReason) {
        this.user = user;
        this.matchScore = matchScore;
        this.matchedOfferedSkill = matchedOfferedSkill;
        this.matchedWantedSkill = matchedWantedSkill;
        this.matchReason = matchReason;
    }

    public UserProfileDto getUser() {
        return user;
    }

    public void setUser(UserProfileDto user) {
        this.user = user;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
        this.matchScore = matchScore;
    }

    public SkillDto getMatchedOfferedSkill() {
        return matchedOfferedSkill;
    }

    public void setMatchedOfferedSkill(SkillDto matchedOfferedSkill) {
        this.matchedOfferedSkill = matchedOfferedSkill;
    }

    public SkillDto getMatchedWantedSkill() {
        return matchedWantedSkill;
    }

    public void setMatchedWantedSkill(SkillDto matchedWantedSkill) {
        this.matchedWantedSkill = matchedWantedSkill;
    }

    public String getMatchReason() {
        return matchReason;
    }

    public void setMatchReason(String matchReason) {
        this.matchReason = matchReason;
    }
}
