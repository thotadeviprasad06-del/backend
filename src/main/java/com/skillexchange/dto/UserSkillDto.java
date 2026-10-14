package com.skillexchange.dto;

import java.time.LocalDateTime;

/**
 * DTO representing a skill associated with a specific user profile (OFFERED or WANTED).
 */
public class UserSkillDto {

    private Long id;
    private Long skillId;
    private String skillName;
    private String category;
    private String skillType; // OFFERED or WANTED
    private String proficiencyLevel; // BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    private String notes;
    private LocalDateTime createdAt;

    public UserSkillDto() {
    }

    public UserSkillDto(Long id, Long skillId, String skillName, String category, String skillType, String proficiencyLevel, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.skillType = skillType;
        this.proficiencyLevel = proficiencyLevel;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
