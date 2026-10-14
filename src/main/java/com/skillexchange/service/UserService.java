package com.skillexchange.service;

import com.skillexchange.dto.UpdateProfileRequest;
import com.skillexchange.dto.UserProfileDto;

import java.util.List;

/**
 * Service interface for managing user profiles and peer search discovery.
 */
public interface UserService {

    UserProfileDto getCurrentUserProfile(String email);

    UserProfileDto updateUserProfile(String email, UpdateProfileRequest request);

    UserProfileDto getUserPublicProfile(Long id);

    List<UserProfileDto> searchUsers(String query, String category, String proficiency, String currentEmail);
}
