package com.skillexchange.service;

import com.skillexchange.dto.AddUserSkillRequest;
import com.skillexchange.dto.UserSkillDto;

import java.util.List;

/**
 * Service interface for managing skills associated with a user profile.
 */
public interface UserSkillService {

    List<UserSkillDto> getCurrentUserSkills(String email);

    List<UserSkillDto> getUserSkillsByUserId(Long userId);

    UserSkillDto addUserSkill(String email, AddUserSkillRequest request);

    void deleteUserSkill(String email, Long userSkillId);
}
