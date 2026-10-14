package com.skillexchange.service.impl;

import com.skillexchange.dto.ConversationDto;
import com.skillexchange.dto.MessageDto;
import com.skillexchange.dto.SendMessageRequest;
import com.skillexchange.entity.*;
import com.skillexchange.exception.BadRequestException;
import com.skillexchange.exception.ResourceNotFoundException;
import com.skillexchange.exception.UnauthorizedException;
import com.skillexchange.repository.ConversationRepository;
import com.skillexchange.repository.ExchangeRequestRepository;
import com.skillexchange.repository.MessageRepository;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.service.ConversationService;
import com.skillexchange.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ConversationService for in-app messaging between exchange partners.
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ExchangeRequestRepository exchangeRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional(readOnly = true)
    public List<ConversationDto> getUserConversations(String email) {
        User user = getUserByEmail(email);
        List<Conversation> conversations = conversationRepository.findUserConversations(user.getId());

        return conversations.stream()
                .map(c -> mapToDto(c, user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ConversationDto getExchangeConversation(String email, Long exchangeId) {
        User user = getUserByEmail(email);
        ExchangeRequest exchange = exchangeRequestRepository.findById(exchangeId)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange request not found with id: " + exchangeId));

        validateParticipant(user, exchange);

        Conversation conv = conversationRepository.findByExchangeRequestId(exchange.getId())
                .orElseGet(() -> conversationRepository.save(new Conversation(exchange)));

        return mapToDto(conv, user.getId());
    }

    @Override
    @Transactional
    public List<MessageDto> getConversationMessages(String email, Long conversationId) {
        User user = getUserByEmail(email);
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found with id: " + conversationId));

        validateParticipant(user, conversation.getExchangeRequest());

        // Mark incoming messages as read
        messageRepository.markConversationMessagesAsRead(conversationId, user.getId());

        List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        return messages.stream()
                .map(this::mapMessageToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageDto sendMessage(String email, Long conversationId, SendMessageRequest request) {
        User sender = getUserByEmail(email);
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found with id: " + conversationId));

        ExchangeRequest exchange = conversation.getExchangeRequest();
        validateParticipant(sender, exchange);

        if (!"ACCEPTED".equalsIgnoreCase(exchange.getStatus()) && !"COMPLETED".equalsIgnoreCase(exchange.getStatus())) {
            throw new BadRequestException("Messages can only be sent on ACCEPTED or COMPLETED exchanges.");
        }

        Message message = new Message(conversation, sender, request.getContent().trim());
        Message saved = messageRepository.save(message);

        // Update conversation timestamp
        conversation.setUpdatedAt(saved.getCreatedAt());
        conversationRepository.save(conversation);

        // Determine the partner to notify
        User recipient = exchange.getRequester().getId().equals(sender.getId())
                ? exchange.getReceiver()
                : exchange.getRequester();

        String notifMsg = sender.getFullName() + ": " + (request.getContent().length() > 40
                ? request.getContent().substring(0, 37) + "..."
                : request.getContent());

        notificationService.sendAsyncNotification(recipient, "New Chat Message", notifMsg, "NEW_MESSAGE", conversationId);

        return mapMessageToDto(saved);
    }

    private void validateParticipant(User user, ExchangeRequest exchange) {
        boolean isParticipant = exchange.getRequester().getId().equals(user.getId())
                || exchange.getReceiver().getId().equals(user.getId());
        if (!isParticipant && !"ROLE_ADMIN".equals(user.getRole())) {
            throw new UnauthorizedException("You are not an authorized participant in this conversation.");
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private ConversationDto mapToDto(Conversation c, Long currentUserId) {
        ConversationDto dto = new ConversationDto();
        dto.setId(c.getId());
        dto.setExchangeRequestId(c.getExchangeRequest().getId());
        dto.setExchangeStatus(c.getExchangeRequest().getStatus());

        User partner = c.getExchangeRequest().getRequester().getId().equals(currentUserId)
                ? c.getExchangeRequest().getReceiver()
                : c.getExchangeRequest().getRequester();

        dto.setPartnerId(partner.getId());
        dto.setPartnerName(partner.getFullName());
        dto.setPartnerEmail(partner.getEmail());

        dto.setOfferedSkillName(c.getExchangeRequest().getOfferedSkill().getName());
        dto.setWantedSkillName(c.getExchangeRequest().getWantedSkill().getName());

        List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(c.getId());
        if (!messages.isEmpty()) {
            Message lastMsg = messages.get(messages.size() - 1);
            dto.setLastMessage(lastMsg.getContent());
            dto.setLastMessageTime(lastMsg.getCreatedAt());
        }

        long unread = messageRepository.countByConversationIdAndSenderIdNotAndIsReadFalse(c.getId(), currentUserId);
        dto.setUnreadCount(unread);

        return dto;
    }

    private MessageDto mapMessageToDto(Message m) {
        return new MessageDto(
                m.getId(),
                m.getConversation().getId(),
                m.getSender().getId(),
                m.getSender().getFullName(),
                m.getContent(),
                m.isRead(),
                m.getCreatedAt()
        );
    }
}
