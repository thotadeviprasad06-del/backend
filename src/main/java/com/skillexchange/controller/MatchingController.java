package com.skillexchange.controller;

import com.skillexchange.dto.UserMatchDto;
import com.skillexchange.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for algorithmic peer recommendations.
 */
@RestController
@RequestMapping("/api/matches")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    @GetMapping("/recommendations")
    public ResponseEntity<List<UserMatchDto>> getRecommendations(Authentication authentication) {
        List<UserMatchDto> recommendations = matchingService.getRecommendationsForUser(authentication.getName());
        return ResponseEntity.ok(recommendations);
    }
}
