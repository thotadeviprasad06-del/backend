package com.skillexchange.service.impl;

import com.skillexchange.dto.SkillDto;
import com.skillexchange.dto.UserMatchDto;
import com.skillexchange.dto.UserProfileDto;
import com.skillexchange.dto.UserSkillDto;
import com.skillexchange.entity.Skill;
import com.skillexchange.entity.User;
import com.skillexchange.entity.UserSkill;
import com.skillexchange.exception.ResourceNotFoundException;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.repository.UserSkillRepository;
import com.skillexchange.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Algorithmic matching service that calculates complementarity scores between learning partners.
 * Explains recommendation rationale clearly for B.Tech project viva evaluation.
 */
@Service
public class MatchingServiceImpl implements MatchingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserMatchDto> getRecommendationsForUser(String email) {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Current user's skills
        List<UserSkill> mySkills = userSkillRepository.findByUserId(currentUser.getId());
        List<UserSkill> myWantedSkills = mySkills.stream()
                .filter(s -> "WANTED".equalsIgnoreCase(s.getSkillType()))
                .collect(Collectors.toList());

        List<UserSkill> myOfferedSkills = mySkills.stream()
                .filter(s -> "OFFERED".equalsIgnoreCase(s.getSkillType()))
                .collect(Collectors.toList());

        Set<Long> myWantedSkillIds = myWantedSkills.stream()
                .map(s -> s.getSkill().getId())
                .collect(Collectors.toSet());

        Set<Long> myOfferedSkillIds = myOfferedSkills.stream()
                .map(s -> s.getSkill().getId())
                .collect(Collectors.toSet());

        Set<String> myWantedCategories = myWantedSkills.stream()
                .map(s -> s.getSkill().getCategory().toLowerCase())
                .collect(Collectors.toSet());

        // Fetch all active potential peer users
        List<User> potentialPeers = userRepository.findAll().stream()
                .filter(u -> u.isActive() && !u.getId().equals(currentUser.getId()) && !"ROLE_ADMIN".equals(u.getRole()))
                .collect(Collectors.toList());

        List<UserMatchDto> matches = new ArrayList<>();

        for (User peer : potentialPeers) {
            List<UserSkill> peerSkills = userSkillRepository.findByUserId(peer.getId());

            List<UserSkill> peerOffered = peerSkills.stream()
                    .filter(s -> "OFFERED".equalsIgnoreCase(s.getSkillType()))
                    .collect(Collectors.toList());

            List<UserSkill> peerWanted = peerSkills.stream()
                    .filter(s -> "WANTED".equalsIgnoreCase(s.getSkillType()))
                    .collect(Collectors.toList());

            // 1. Check if peer offers a skill I want (Direct overlap)
            Optional<UserSkill> directTeachMatch = peerOffered.stream()
                    .filter(s -> myWantedSkillIds.contains(s.getSkill().getId()))
                    .findFirst();

            // 2. Check if I offer a skill peer wants (Mutual overlap)
            Optional<UserSkill> directLearnMatch = peerWanted.stream()
                    .filter(s -> myOfferedSkillIds.contains(s.getSkill().getId()))
                    .findFirst();

            // 3. Category level match
            Optional<UserSkill> categoryMatch = peerOffered.stream()
                    .filter(s -> myWantedCategories.contains(s.getSkill().getCategory().toLowerCase()))
                    .findFirst();

            int score = 0;
            Skill matchedOfferedSkill = null;
            Skill matchedWantedSkill = null;
            String reason = null;

            if (directTeachMatch.isPresent() && directLearnMatch.isPresent()) {
                // Perfect two-way barter match
                score = 100;
                matchedOfferedSkill = directTeachMatch.get().getSkill();
                matchedWantedSkill = directLearnMatch.get().getSkill();
                reason = "Perfect Match: " + peer.getFullName() + " teaches " + matchedOfferedSkill.getName()
                        + " (which you want to learn), and wants to learn " + matchedWantedSkill.getName()
                        + " (which you can teach).";
            } else if (directTeachMatch.isPresent()) {
                // One-way direct match
                score = 75;
                matchedOfferedSkill = directTeachMatch.get().getSkill();
                if (!myOfferedSkills.isEmpty()) {
                    matchedWantedSkill = myOfferedSkills.get(0).getSkill();
                }
                reason = "Great Match: " + peer.getFullName() + " teaches " + matchedOfferedSkill.getName()
                        + ", which you have listed on your wanted skills list.";
            } else if (categoryMatch.isPresent()) {
                // Category similarity
                score = 50;
                matchedOfferedSkill = categoryMatch.get().getSkill();
                reason = "Category Match: " + peer.getFullName() + " offers skills in "
                        + matchedOfferedSkill.getCategory() + ", aligning with your learning interests.";
            } else if (!peerOffered.isEmpty()) {
                // Discoverable peer
                score = 30;
                matchedOfferedSkill = peerOffered.get(0).getSkill();
                reason = "Available Peer: " + peer.getFullName() + " has skills available for exchange.";
            }

            if (score > 0) {
                UserProfileDto peerProfile = buildUserProfileDto(peer, peerSkills);
                SkillDto offeredDto = matchedOfferedSkill != null ? mapSkillToDto(matchedOfferedSkill) : null;
                SkillDto wantedDto = matchedWantedSkill != null ? mapSkillToDto(matchedWantedSkill) : null;

                matches.add(new UserMatchDto(peerProfile, score, offeredDto, wantedDto, reason));
            }
        }

        // Sort descending by highest match score
        matches.sort((a, b) -> Integer.compare(b.getMatchScore(), a.getMatchScore()));

        return matches;
    }

    private UserProfileDto buildUserProfileDto(User user, List<UserSkill> skills) {
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
