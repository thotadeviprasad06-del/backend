package com.skillexchange.service.impl;

import com.skillexchange.dto.UpdateProfileRequest;
import com.skillexchange.dto.UserProfileDto;
import com.skillexchange.dto.UserSkillDto;
import com.skillexchange.entity.User;
import com.skillexchange.entity.UserSkill;
import com.skillexchange.exception.ResourceNotFoundException;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.repository.UserSkillRepository;
import com.skillexchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of UserService for profile management and peer search.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUserProfile(String email) {
        User user = getUserByEmail(email);
        return buildFullProfileDto(user);
    }

    @Override
    @Transactional
    public UserProfileDto updateUserProfile(String email, UpdateProfileRequest request) {
        User user = getUserByEmail(email);

        user.setFullName(request.getFullName().trim());
        user.setBio(request.getBio());
        if (request.getPreferredLearningMode() != null) {
            user.setPreferredLearningMode(request.getPreferredLearningMode());
        }

        User updated = userRepository.save(user);
        return buildFullProfileDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getUserPublicProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return buildFullProfileDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileDto> searchUsers(String query, String category, String proficiency, String currentEmail) {
        User currentUser = getUserByEmail(currentEmail);

        // Fetch all active users except the current user and admins
        List<User> activeUsers = userRepository.findAll().stream()
                .filter(u -> u.isActive() && !u.getId().equals(currentUser.getId()) && !"ROLE_ADMIN".equals(u.getRole()))
                .collect(Collectors.toList());

        String q = (query != null) ? query.trim().toLowerCase() : "";
        String cat = (category != null) ? category.trim().toLowerCase() : "";
        String prof = (proficiency != null) ? proficiency.trim().toUpperCase() : "";

        return activeUsers.stream()
                .map(this::buildFullProfileDto)
                .filter(profile -> {
                    // Match query across name, bio, or skill names
                    boolean matchesQuery = q.isEmpty()
                            || profile.getFullName().toLowerCase().contains(q)
                            || (profile.getBio() != null && profile.getBio().toLowerCase().contains(q))
                            || profile.getSkillsOffered().stream().anyMatch(s -> s.getSkillName().toLowerCase().contains(q))
                            || profile.getSkillsWanted().stream().anyMatch(s -> s.getSkillName().toLowerCase().contains(q));

                    // Match category
                    boolean matchesCategory = cat.isEmpty()
                            || profile.getSkillsOffered().stream().anyMatch(s -> s.getCategory().toLowerCase().equals(cat))
                            || profile.getSkillsWanted().stream().anyMatch(s -> s.getCategory().toLowerCase().equals(cat));

                    // Match proficiency
                    boolean matchesProficiency = prof.isEmpty()
                            || profile.getSkillsOffered().stream().anyMatch(s -> s.getProficiencyLevel().equalsIgnoreCase(prof));

                    return matchesQuery && matchesCategory && matchesProficiency;
                })
                .collect(Collectors.toList());
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
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
}
