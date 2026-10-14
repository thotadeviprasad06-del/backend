package com.skillexchange.controller;

import com.skillexchange.dto.ConversationDto;
import com.skillexchange.dto.MessageDto;
import com.skillexchange.dto.SendMessageRequest;
import com.skillexchange.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for direct chat messages and conversation rooms.
 */
@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping
    public ResponseEntity<List<ConversationDto>> getMyConversations(Authentication authentication) {
        List<ConversationDto> conversations = conversationService.getUserConversations(authentication.getName());
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/exchange/{exchangeId}")
    public ResponseEntity<ConversationDto> getExchangeConversation(
            @PathVariable Long exchangeId,
            Authentication authentication) {
        ConversationDto conv = conversationService.getExchangeConversation(authentication.getName(), exchangeId);
        return ResponseEntity.ok(conv);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageDto>> getMessages(
            @PathVariable Long id,
            Authentication authentication) {
        List<MessageDto> messages = conversationService.getConversationMessages(authentication.getName(), id);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageDto> sendMessage(
            @PathVariable Long id,
            @Valid @RequestBody SendMessageRequest request,
            Authentication authentication) {
        MessageDto sent = conversationService.sendMessage(authentication.getName(), id, request);
        return new ResponseEntity<>(sent, HttpStatus.CREATED);
    }
}
