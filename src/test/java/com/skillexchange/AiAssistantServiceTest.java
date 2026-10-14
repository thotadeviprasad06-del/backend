package com.skillexchange;

import com.skillexchange.dto.AiChatRequest;
import com.skillexchange.dto.AiChatResponse;
import com.skillexchange.service.AiAssistantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AiAssistantServiceTest {

    @Autowired
    private AiAssistantService aiAssistantService;

    @Test
    @DisplayName("Should retrieve grounded context for skill addition question")
    void testGroundedResponseForSkillQuestion() {
        AiChatRequest request = new AiChatRequest("How do I add a new skill to my profile?");
        AiChatResponse response = aiAssistantService.askAssistant(request);

        assertNotNull(response);
        assertNotNull(response.getAnswer());
        assertFalse(response.getRetrievedSources().isEmpty());
        assertTrue(response.getAnswer().toLowerCase().contains("my skills"));
        assertTrue(response.getConfidenceScore() > 0.0);
    }

    @Test
    @DisplayName("Should handle irrelevant out-of-scope question gracefully")
    void testIrrelevantQuestionHandling() {
        AiChatRequest request = new AiChatRequest("What is the current stock price of Apple?");
        AiChatResponse response = aiAssistantService.askAssistant(request);

        assertNotNull(response);
        assertTrue(response.getAnswer().contains("Skill Exchange Assistant"));
        assertEquals(0.0, response.getConfidenceScore());
    }
}
