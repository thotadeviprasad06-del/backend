package com.skillexchange.service.impl;

import com.skillexchange.dto.AddUserSkillRequest;
import com.skillexchange.dto.UserSkillDto;
import com.skillexchange.entity.Skill;
import com.skillexchange.entity.User;
import com.skillexchange.entity.UserSkill;
import com.skillexchange.exception.ConflictException;
import com.skillexchange.exception.ResourceNotFoundException;
import com.skillexchange.exception.UnauthorizedException;
import com.skillexchange.repository.SkillRepository;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.repository.UserSkillRepository;
import com.skillexchange.service.UserSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of UserSkillService.
 */
@Service
public class UserSkillServiceImpl implements UserSkillService {

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserSkillDto> getCurrentUserSkills(String email) {
        User user = getUserByEmail(email);
        return userSkillRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSkillDto> getUserSkillsByUserId(Long userId) {
        return userSkillRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserSkillDto addUserSkill(String email, AddUserSkillRequest request) {
        User user = getUserByEmail(email);

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + request.getSkillId()));

        if (userSkillRepository.existsByUserIdAndSkillIdAndSkillType(user.getId(), skill.getId(), request.getSkillType())) {
            throw new ConflictException("You have already listed '" + skill.getName() + "' under " + request.getSkillType());
        }

        UserSkill userSkill = new UserSkill(
                user,
                skill,
                request.getSkillType().toUpperCase(),
                request.getProficiencyLevel().toUpperCase(),
                request.getNotes()
        );

        UserSkill saved = userSkillRepository.save(userSkill);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public void deleteUserSkill(String email, Long userSkillId) {
        User user = getUserByEmail(email);

        UserSkill userSkill = userSkillRepository.findById(userSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("User skill entry not found with id: " + userSkillId));

        if (!userSkill.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to delete another user's skill listing");
        }

        userSkillRepository.delete(userSkill);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private UserSkillDto mapToDto(UserSkill us) {
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
