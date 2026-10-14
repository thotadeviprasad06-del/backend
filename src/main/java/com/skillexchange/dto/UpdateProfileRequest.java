package com.skillexchange.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for updating user profile bio and preferences.
 */
public class UpdateProfileRequest {

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    private String bio;

    private String preferredLearningMode; // "ONLINE", "IN_PERSON", "HYBRID"

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(String fullName, String bio, String preferredLearningMode) {
        this.fullName = fullName;
        this.bio = bio;
        this.preferredLearningMode = preferredLearningMode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
}
