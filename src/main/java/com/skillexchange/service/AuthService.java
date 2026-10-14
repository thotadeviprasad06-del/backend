package com.skillexchange.service;

import com.skillexchange.dto.AuthRequest;
import com.skillexchange.dto.AuthResponse;
import com.skillexchange.dto.RegisterRequest;

/**
 * Service interface for authentication operations.
 */
public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(AuthRequest request);
}
