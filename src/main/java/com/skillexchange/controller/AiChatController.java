package com.skillexchange.controller;

import com.skillexchange.dto.AiChatRequest;
import com.skillexchange.dto.AiChatResponse;
import com.skillexchange.service.AiAssistantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for the grounded Retrieval-Augmented Generation (RAG) assistant.
 */
@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    @Autowired
    private AiAssistantService aiAssistantService;

    @PostMapping("/chat")
    public ResponseEntity<AiChatResponse> askAssistant(@Valid @RequestBody AiChatRequest request) {
        AiChatResponse response = aiAssistantService.askAssistant(request);
        return ResponseEntity.ok(response);
    }
}
