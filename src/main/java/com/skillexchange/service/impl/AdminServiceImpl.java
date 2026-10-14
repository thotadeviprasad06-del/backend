package com.skillexchange.service.impl;

import com.skillexchange.dto.SkillDto;
import com.skillexchange.dto.UserProfileDto;
import com.skillexchange.dto.UserSkillDto;
import com.skillexchange.entity.Skill;
import com.skillexchange.entity.User;
import com.skillexchange.entity.UserSkill;
import com.skillexchange.exception.BadRequestException;
import com.skillexchange.exception.ResourceNotFoundException;
import com.skillexchange.repository.SkillRepository;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.repository.UserSkillRepository;
import com.skillexchange.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of AdminService for account moderation and catalog management.
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileDto> getAllUsers() {
        return userRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::buildFullProfileDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserProfileDto toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if ("ROLE_ADMIN".equals(user.getRole())) {
            throw new BadRequestException("Administrator account status cannot be toggled.");
        }

        user.setActive(!user.isActive());
        User updated = userRepository.save(user);
        return buildFullProfileDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDto> getAllSkillsAdmin() {
        return skillRepository.findAll().stream()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .map(this::mapSkillToDto)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "skillCategories", allEntries = true)
    @Transactional
    public SkillDto toggleSkillStatus(Long skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + skillId));

        skill.setActive(!skill.isActive());
        Skill updated = skillRepository.save(skill);
        return mapSkillToDto(updated);
    }

    private UserProfileDto buildFullProfileDto(User user) {
        UserProfileDto dto = new UserProfileDto(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.getBio(),
                user.getPreferredLearningMode(),
                user.isActive(),
                user.getCreatedAt()
        );

        List<UserSkill> skills = userSkillRepository.findByUserId(user.getId());

        List<UserSkillDto> offered = skills.stream()
                .filter(s -> "OFFERED".equalsIgnoreCase(s.getSkillType()))
                .map(this::mapUserSkillToDto)
                .collect(Collectors.toList());

        List<UserSkillDto> wanted = skills.stream()
                .filter(s -> "WANTED".equalsIgnoreCase(s.getSkillType()))
                .map(this::mapUserSkillToDto)
                .collect(Collectors.toList());

        dto.setSkillsOffered(offered);
        dto.setSkillsWanted(wanted);

        return dto;
    }

    private UserSkillDto mapUserSkillToDto(UserSkill us) {
        return new UserSkillDto(
                us.getId(),
                us.getSkill().getId(),
                us.getSkill().getName(),
                us.getSkill().getCategory(),
                us.getSkillType(),
                us.getProficiencyLevel(),
                us.getNotes(),
                us.getCreatedAt()
        );
    }

    private SkillDto mapSkillToDto(Skill s) {
        return new SkillDto(
                s.getId(),
                s.getName(),
                s.getCategory(),
                s.getDescription(),
                s.isActive(),
                s.getCreatedAt()
        );
    }
}
