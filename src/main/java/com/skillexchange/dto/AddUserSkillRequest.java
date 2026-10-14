package com.skillexchange.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO for adding a skill to the authenticated user's profile.
 */
public class AddUserSkillRequest {

    @NotNull(message = "Skill ID is required")
    private Long skillId;

    @NotBlank(message = "Skill type is required (OFFERED or WANTED)")
    @Pattern(regexp = "OFFERED|WANTED", message = "Skill type must be OFFERED or WANTED")
    private String skillType;

    @NotBlank(message = "Proficiency level is required")
    @Pattern(regexp = "BEGINNER|INTERMEDIATE|ADVANCED|EXPERT", message = "Proficiency level must be BEGINNER, INTERMEDIATE, ADVANCED, or EXPERT")
    private String proficiencyLevel;

    private String notes;

    public AddUserSkillRequest() {
    }

    public AddUserSkillRequest(Long skillId, String skillType, String proficiencyLevel, String notes) {
        this.skillId = skillId;
        this.skillType = skillType;
        this.proficiencyLevel = proficiencyLevel;
        this.notes = notes;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
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
}
