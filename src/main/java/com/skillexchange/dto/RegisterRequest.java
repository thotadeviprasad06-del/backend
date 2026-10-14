package com.skillexchange.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for user account registration.
 */
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    private String bio;

    private String preferredLearningMode; // "ONLINE", "IN_PERSON", "HYBRID"

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password, String fullName, String bio, String preferredLearningMode) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.bio = bio;
        this.preferredLearningMode = preferredLearningMode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
