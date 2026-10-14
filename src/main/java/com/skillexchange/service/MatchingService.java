package com.skillexchange.service;

import com.skillexchange.dto.UserMatchDto;

import java.util.List;

/**
 * Service interface for the algorithmic peer matchmaking engine.
 */
public interface MatchingService {

    List<UserMatchDto> getRecommendationsForUser(String email);
}
