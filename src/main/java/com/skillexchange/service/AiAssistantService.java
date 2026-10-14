package com.skillexchange.service;

import com.skillexchange.dto.AiChatRequest;
import com.skillexchange.dto.AiChatResponse;

/**
 * Service interface for the grounded Retrieval-Augmented Generation (RAG) AI assistant.
 */
public interface AiAssistantService {

    AiChatResponse askAssistant(AiChatRequest request);
}
