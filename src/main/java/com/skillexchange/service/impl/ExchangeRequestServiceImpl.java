package com.skillexchange.service.impl;

import com.skillexchange.dto.CreateExchangeRequest;
import com.skillexchange.dto.ExchangeResponseDto;
import com.skillexchange.dto.SkillDto;
import com.skillexchange.entity.*;
import com.skillexchange.exception.BadRequestException;
import com.skillexchange.exception.ConflictException;
import com.skillexchange.exception.ResourceNotFoundException;
import com.skillexchange.exception.UnauthorizedException;
import com.skillexchange.repository.*;
import com.skillexchange.service.ExchangeRequestService;
import com.skillexchange.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ExchangeRequestService managing the end-to-end skill exchange workflow.
 */
@Service
public class ExchangeRequestServiceImpl implements ExchangeRequestService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRequestServiceImpl.class);

    @Autowired
    private ExchangeRequestRepository exchangeRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public ExchangeResponseDto createExchange(String email, CreateExchangeRequest request) {
        User requester = getUserByEmail(email);

        if (requester.getId().equals(request.getReceiverId())) {
            throw new BadRequestException("You cannot initiate a skill exchange request with yourself.");
        }

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver user not found with id: " + request.getReceiverId()));

        if (!receiver.isActive()) {
            throw new BadRequestException("The target user account is currently inactive.");
        }

        Skill offeredSkill = skillRepository.findById(request.getOfferedSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Offered skill not found with id: " + request.getOfferedSkillId()));

        Skill wantedSkill = skillRepository.findById(request.getWantedSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Wanted skill not found with id: " + request.getWantedSkillId()));

        if (offeredSkill.getId().equals(wantedSkill.getId())) {
            throw new BadRequestException("Offered skill and requested skill cannot be identical in an exchange.");
        }

        // Prevent duplicate pending requests
        boolean alreadyPending = exchangeRequestRepository.existsByRequesterIdAndReceiverIdAndOfferedSkillIdAndWantedSkillIdAndStatus(
                requester.getId(), receiver.getId(), offeredSkill.getId(), wantedSkill.getId(), "PENDING");

        if (alreadyPending) {
            throw new ConflictException("You already have a pending exchange request with this user for these skills.");
        }

        ExchangeRequest exchangeRequest = new ExchangeRequest(
                requester,
                receiver,
                offeredSkill,
                wantedSkill,
                request.getMessage()
        );

        ExchangeRequest saved = exchangeRequestRepository.save(exchangeRequest);

        // Notify the receiver asynchronously
        String notifMsg = requester.getFullName() + " wants to learn " + wantedSkill.getName()
                + " and offered to teach " + offeredSkill.getName() + " in exchange.";
        notificationService.sendAsyncNotification(receiver, "New Skill Exchange Request", notifMsg, "REQUEST_RECEIVED", saved.getId());

        logger.info("Created exchange request ID {} from user {} to user {}", saved.getId(), requester.getId(), receiver.getId());
        return mapToDto(saved, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExchangeResponseDto> getIncomingRequests(String email) {
        User receiver = getUserByEmail(email);
        List<ExchangeRequest> requests = exchangeRequestRepository.findByReceiverIdOrderByCreatedAtDesc(receiver.getId());

        return requests.stream()
                .map(req -> mapToDto(req, findConversationId(req.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExchangeResponseDto> getOutgoingRequests(String email) {
        User requester = getUserByEmail(email);
        List<ExchangeRequest> requests = exchangeRequestRepository.findByRequesterIdOrderByCreatedAtDesc(requester.getId());

        return requests.stream()
                .map(req -> mapToDto(req, findConversationId(req.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ExchangeResponseDto getExchangeById(String email, Long id) {
        User currentUser = getUserByEmail(email);
        ExchangeRequest req = exchangeRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange request not found with id: " + id));

        boolean isParticipant = req.getRequester().getId().equals(currentUser.getId()) ||
                                req.getReceiver().getId().equals(currentUser.getId());

        boolean isAdmin = "ROLE_ADMIN".equals(currentUser.getRole());

        if (!isParticipant && !isAdmin) {
            throw new UnauthorizedException("You are not authorized to view this exchange request");
        }

        return mapToDto(req, findConversationId(req.getId()));
    }

    @Override
    @Transactional
    public ExchangeResponseDto acceptExchange(String email, Long id) {
        User receiver = getUserByEmail(email);
        ExchangeRequest req = exchangeRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange request not found with id: " + id));

        if (!req.getReceiver().getId().equals(receiver.getId())) {
            throw new UnauthorizedException("Only the designated recipient can accept this exchange request.");
        }

        if (!"PENDING".equalsIgnoreCase(req.getStatus())) {
            throw new BadRequestException("Only PENDING requests can be accepted. Current status: " + req.getStatus());
        }

        req.setStatus("ACCEPTED");
        ExchangeRequest updated = exchangeRequestRepository.save(req);

        // Create associated conversation thread if not already created
        Conversation conv = conversationRepository.findByExchangeRequestId(updated.getId())
                .orElseGet(() -> conversationRepository.save(new Conversation(updated)));

        // Notify requester asynchronously
        String notifMsg = receiver.getFullName() + " accepted your exchange request! You can now chat and coordinate learning sessions.";
        notificationService.sendAsyncNotification(req.getRequester(), "Exchange Request Accepted", notifMsg, "REQUEST_ACCEPTED", updated.getId());

        logger.info("Exchange request ID {} accepted by receiver ID {}", updated.getId(), receiver.getId());
        return mapToDto(updated, conv.getId());
    }

    @Override
    @Transactional
    public ExchangeResponseDto rejectExchange(String email, Long id) {
        User receiver = getUserByEmail(email);
        ExchangeRequest req = exchangeRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange request not found with id: " + id));

        if (!req.getReceiver().getId().equals(receiver.getId())) {
            throw new UnauthorizedException("Only the designated recipient can reject this exchange request.");
        }

        if (!"PENDING".equalsIgnoreCase(req.getStatus())) {
            throw new BadRequestException("Only PENDING requests can be rejected. Current status: " + req.getStatus());
        }

        req.setStatus("REJECTED");
        ExchangeRequest updated = exchangeRequestRepository.save(req);

        // Notify requester asynchronously
        String notifMsg = receiver.getFullName() + " declined your exchange request.";
        notificationService.sendAsyncNotification(req.getRequester(), "Exchange Request Declined", notifMsg, "REQUEST_REJECTED", updated.getId());

        logger.info("Exchange request ID {} rejected by receiver ID {}", updated.getId(), receiver.getId());
        return mapToDto(updated, null);
    }

    @Override
    @Transactional
    public ExchangeResponseDto cancelExchange(String email, Long id) {
        User requester = getUserByEmail(email);
        ExchangeRequest req = exchangeRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange request not found with id: " + id));

        if (!req.getRequester().getId().equals(requester.getId())) {
            throw new UnauthorizedException("Only the requester can cancel this outgoing request.");
        }

        if (!"PENDING".equalsIgnoreCase(req.getStatus())) {
            throw new BadRequestException("Only PENDING requests can be cancelled. Current status: " + req.getStatus());
        }

        req.setStatus("CANCELLED");
        ExchangeRequest updated = exchangeRequestRepository.save(req);
        logger.info("Exchange request ID {} cancelled by requester ID {}", updated.getId(), requester.getId());
        return mapToDto(updated, null);
    }

    @Override
    @Transactional
    public ExchangeResponseDto completeExchange(String email, Long id) {
        User currentUser = getUserByEmail(email);
        ExchangeRequest req = exchangeRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange request not found with id: " + id));

        boolean isRequester = req.getRequester().getId().equals(currentUser.getId());
        boolean isReceiver = req.getReceiver().getId().equals(currentUser.getId());

        if (!isRequester && !isReceiver) {
            throw new UnauthorizedException("You are not a participant in this exchange.");
        }

        if (!"ACCEPTED".equalsIgnoreCase(req.getStatus())) {
            throw new BadRequestException("Only ACCEPTED active exchanges can be marked as COMPLETED. Current status: " + req.getStatus());
        }

        req.setStatus("COMPLETED");
        ExchangeRequest updated = exchangeRequestRepository.save(req);

        // Notify the other partner asynchronously
        User partner = isRequester ? req.getReceiver() : req.getRequester();
        String notifMsg = currentUser.getFullName() + " marked your skill exchange as COMPLETED. Congratulations on your learning journey!";
        notificationService.sendAsyncNotification(partner, "Exchange Completed", notifMsg, "EXCHANGE_COMPLETED", updated.getId());

        logger.info("Exchange request ID {} marked COMPLETED by user ID {}", updated.getId(), currentUser.getId());
        return mapToDto(updated, findConversationId(updated.getId()));
    }

    private Long findConversationId(Long exchangeId) {
        return conversationRepository.findByExchangeRequestId(exchangeId)
                .map(Conversation::getId)
                .orElse(null);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
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
