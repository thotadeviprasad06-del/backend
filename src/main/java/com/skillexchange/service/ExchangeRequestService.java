package com.skillexchange.service;

import com.skillexchange.dto.CreateExchangeRequest;
import com.skillexchange.dto.ExchangeResponseDto;

import java.util.List;

/**
 * Service interface for the skill exchange workflow state machine.
 */
public interface ExchangeRequestService {

    ExchangeResponseDto createExchange(String email, CreateExchangeRequest request);

    List<ExchangeResponseDto> getIncomingRequests(String email);

    List<ExchangeResponseDto> getOutgoingRequests(String email);

    ExchangeResponseDto getExchangeById(String email, Long id);

    ExchangeResponseDto acceptExchange(String email, Long id);

    ExchangeResponseDto rejectExchange(String email, Long id);

    ExchangeResponseDto cancelExchange(String email, Long id);

    ExchangeResponseDto completeExchange(String email, Long id);
}
