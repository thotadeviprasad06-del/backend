package com.skillexchange.service.impl;

import com.skillexchange.dto.*;
import com.skillexchange.entity.ExchangeRequest;
import com.skillexchange.entity.Skill;
import com.skillexchange.entity.User;
import com.skillexchange.exception.ResourceNotFoundException;
import com.skillexchange.repository.*;
import com.skillexchange.service.DashboardService;
import com.skillexchange.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DashboardService calculating live database metrics for users and admins.
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private ExchangeRequestRepository exchangeRequestRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MatchingService matchingService;

    @Override
    @Transactional(readOnly = true)
    public UserDashboardDto getUserDashboard(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        UserDashboardDto dto = new UserDashboardDto();
        dto.setUserFullName(user.getFullName());

        long offered = userSkillRepository.countByUserIdAndSkillType(user.getId(), "OFFERED");
        long wanted = userSkillRepository.countByUserIdAndSkillType(user.getId(), "WANTED");
        dto.setSkillsOfferedCount(offered);
        dto.setSkillsWantedCount(wanted);

        long incoming = exchangeRequestRepository.countByReceiverIdAndStatus(user.getId(), "PENDING");
        long outgoing = exchangeRequestRepository.countByRequesterIdAndStatus(user.getId(), "PENDING");
        dto.setIncomingRequestsCount(incoming);
        dto.setOutgoingRequestsCount(outgoing);

        dto.setPendingExchangesCount(incoming + outgoing);
        dto.setAcceptedExchangesCount(exchangeRequestRepository.countUserExchangesByStatus(user.getId(), "ACCEPTED"));
        dto.setCompletedExchangesCount(exchangeRequestRepository.countUserExchangesByStatus(user.getId(), "COMPLETED"));

        dto.setUnreadNotificationsCount(notificationRepository.countByUserIdAndIsReadFalse(user.getId()));

        // Recent exchanges
        List<ExchangeRequest> allUserExchanges = exchangeRequestRepository.findAllUserExchanges(user.getId());
        List<ExchangeResponseDto> recentExchanges = allUserExchanges.stream()
                .limit(5)
                .map(req -> mapToDto(req, findConversationId(req.getId())))
                .collect(Collectors.toList());
        dto.setRecentExchanges(recentExchanges);

        // Top 3 partner recommendations
        List<UserMatchDto> recommendations = matchingService.getRecommendationsForUser(email);
        dto.setTopRecommendations(recommendations.stream().limit(3).collect(Collectors.toList()));

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardDto getAdminDashboard() {
        AdminDashboardDto dto = new AdminDashboardDto();

        dto.setTotalUsers(userRepository.count());
        dto.setActiveUsers(userRepository.countByIsActive(true));
        dto.setTotalSkills(skillRepository.count());

        dto.setPendingRequests(exchangeRequestRepository.countByStatus("PENDING"));
        dto.setAcceptedExchanges(exchangeRequestRepository.countByStatus("ACCEPTED"));
        dto.setCompletedExchanges(exchangeRequestRepository.countByStatus("COMPLETED"));
        dto.setTotalMessages(messageRepository.count());

        List<ExchangeRequest> recent = exchangeRequestRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(6)
                .collect(Collectors.toList());

        dto.setRecentSystemExchanges(recent.stream()
                .map(req -> mapToDto(req, findConversationId(req.getId())))
                .collect(Collectors.toList()));

        return dto;
    }

    private Long findConversationId(Long exchangeId) {
        return conversationRepository.findByExchangeRequestId(exchangeId)
                .map(c -> c.getId())
                .orElse(null);
    }

    private ExchangeResponseDto mapToDto(ExchangeRequest req, Long conversationId) {
        ExchangeResponseDto dto = new ExchangeResponseDto();
        dto.setId(req.getId());
        dto.setRequesterId(req.getRequester().getId());
        dto.setRequesterName(req.getRequester().getFullName());
        dto.setRequesterEmail(req.getRequester().getEmail());
        dto.setReceiverId(req.getReceiver().getId());
        dto.setReceiverName(req.getReceiver().getFullName());
        dto.setReceiverEmail(req.getReceiver().getEmail());
        dto.setOfferedSkill(mapSkillToDto(req.getOfferedSkill()));
        dto.setWantedSkill(mapSkillToDto(req.getWantedSkill()));
        dto.setMessage(req.getMessage());
        dto.setStatus(req.getStatus());
        dto.setConversationId(conversationId);
        dto.setCreatedAt(req.getCreatedAt());
        dto.setUpdatedAt(req.getUpdatedAt());
        return dto;
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
