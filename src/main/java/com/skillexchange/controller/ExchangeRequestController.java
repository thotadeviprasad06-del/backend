package com.skillexchange.controller;

import com.skillexchange.dto.CreateExchangeRequest;
import com.skillexchange.dto.ExchangeResponseDto;
import com.skillexchange.service.ExchangeRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for skill exchange request operations.
 */
@RestController
@RequestMapping("/api/exchanges")
public class ExchangeRequestController {

    @Autowired
    private ExchangeRequestService exchangeRequestService;

    @PostMapping
    public ResponseEntity<ExchangeResponseDto> createExchange(
            @Valid @RequestBody CreateExchangeRequest request,
            Authentication authentication) {
        ExchangeResponseDto created = exchangeRequestService.createExchange(authentication.getName(), request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/incoming")
    public ResponseEntity<List<ExchangeResponseDto>> getIncomingRequests(Authentication authentication) {
        List<ExchangeResponseDto> incoming = exchangeRequestService.getIncomingRequests(authentication.getName());
        return ResponseEntity.ok(incoming);
    }

    @GetMapping("/outgoing")
    public ResponseEntity<List<ExchangeResponseDto>> getOutgoingRequests(Authentication authentication) {
        List<ExchangeResponseDto> outgoing = exchangeRequestService.getOutgoingRequests(authentication.getName());
        return ResponseEntity.ok(outgoing);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExchangeResponseDto> getExchangeById(
            @PathVariable Long id,
            Authentication authentication) {
        ExchangeResponseDto exchange = exchangeRequestService.getExchangeById(authentication.getName(), id);
        return ResponseEntity.ok(exchange);
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<ExchangeResponseDto> acceptExchange(
            @PathVariable Long id,
            Authentication authentication) {
        ExchangeResponseDto accepted = exchangeRequestService.acceptExchange(authentication.getName(), id);
        return ResponseEntity.ok(accepted);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ExchangeResponseDto> rejectExchange(
            @PathVariable Long id,
            Authentication authentication) {
        ExchangeResponseDto rejected = exchangeRequestService.rejectExchange(authentication.getName(), id);
        return ResponseEntity.ok(rejected);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ExchangeResponseDto> cancelExchange(
            @PathVariable Long id,
            Authentication authentication) {
        ExchangeResponseDto cancelled = exchangeRequestService.cancelExchange(authentication.getName(), id);
        return ResponseEntity.ok(cancelled);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ExchangeResponseDto> completeExchange(
            @PathVariable Long id,
            Authentication authentication) {
        ExchangeResponseDto completed = exchangeRequestService.completeExchange(authentication.getName(), id);
        return ResponseEntity.ok(completed);
    }
}
