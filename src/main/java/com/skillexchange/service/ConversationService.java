package com.skillexchange.service;

import com.skillexchange.dto.ConversationDto;
import com.skillexchange.dto.MessageDto;
import com.skillexchange.dto.SendMessageRequest;

import java.util.List;

/**
 * Service interface for direct chat between exchange partners.
 */
public interface ConversationService {

    List<ConversationDto> getUserConversations(String email);

    ConversationDto getExchangeConversation(String email, Long exchangeId);

    List<MessageDto> getConversationMessages(String email, Long conversationId);

    MessageDto sendMessage(String email, Long conversationId, SendMessageRequest request);
}
