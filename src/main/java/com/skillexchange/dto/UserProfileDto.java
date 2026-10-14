package com.skillexchange.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for user profile representations.
 * Never exposes passwords or internal hashes.
 */
public class UserProfileDto {

    private Long id;
    private String email;
    private String fullName;
    private String role;
    private String bio;
    private String preferredLearningMode;
    private boolean isActive;
    private LocalDateTime createdAt;

    private List<UserSkillDto> skillsOffered = new ArrayList<>();
    private List<UserSkillDto> skillsWanted = new ArrayList<>();

    public UserProfileDto() {
    }

    public UserProfileDto(Long id, String email, String fullName, String role, String bio, String preferredLearningMode, boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.bio = bio;
        this.preferredLearningMode = preferredLearningMode;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPreferredLearningMode() {
        return preferredLearningMode;
    }

    public void setPreferredLearningMode(String preferredLearningMode) {
        this.preferredLearningMode = preferredLearningMode;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<UserSkillDto> getSkillsOffered() {
        return skillsOffered;
    }

    public void setSkillsOffered(List<UserSkillDto> skillsOffered) {
        this.skillsOffered = skillsOffered;
    }

    public List<UserSkillDto> getSkillsWanted() {
        return skillsWanted;
    }

    public void setSkillsWanted(List<UserSkillDto> skillsWanted) {
        this.skillsWanted = skillsWanted;
    }
}
