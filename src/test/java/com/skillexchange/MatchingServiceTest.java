package com.skillexchange;

import com.skillexchange.dto.UserMatchDto;
import com.skillexchange.service.MatchingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MatchingServiceTest {

    @Autowired
    private MatchingService matchingService;

    @Test
    @DisplayName("Should recommend Priya as top mutual match for Rahul")
    void testMatchingAlgorithmRecommendations() {
        // Rahul offers Java & Spring Boot; wants React & UI/UX.
        // Priya offers React & UI/UX; wants Java & Python.
        List<UserMatchDto> matches = matchingService.getRecommendationsForUser("rahul@example.com");

        assertNotNull(matches);
        assertFalse(matches.isEmpty());

        UserMatchDto topMatch = matches.get(0);
        assertEquals("priya@example.com", topMatch.getUser().getEmail());
        assertEquals(100, topMatch.getMatchScore());
        assertTrue(topMatch.getMatchReason().contains("Perfect Match"));
    }
}
