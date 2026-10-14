package com.skillexchange.service;

import com.skillexchange.dto.SkillDto;
import com.skillexchange.dto.UserProfileDto;

import java.util.List;

/**
 * Service interface for platform administrator operations.
 */
public interface AdminService {

    List<UserProfileDto> getAllUsers();

    UserProfileDto toggleUserStatus(Long userId);

    List<SkillDto> getAllSkillsAdmin();

    SkillDto toggleSkillStatus(Long skillId);
}
