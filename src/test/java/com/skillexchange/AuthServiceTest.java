package com.skillexchange;

import com.skillexchange.dto.AuthRequest;
import com.skillexchange.dto.AuthResponse;
import com.skillexchange.dto.RegisterRequest;
import com.skillexchange.exception.ConflictException;
import com.skillexchange.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("Should successfully register a new user and return JWT token")
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest(
                "newstudent@example.com",
                "secret123",
                "New Student",
                "Excited to learn new skills!",
                "ONLINE"
        );

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("newstudent@example.com", response.getEmail());
        assertEquals("New Student", response.getFullName());
        assertEquals("ROLE_USER", response.getRole());
    }

    @Test
    @DisplayName("Should reject registration with duplicate email")
    void testRegisterDuplicateEmail() {
        RegisterRequest request1 = new RegisterRequest(
                "duplicate@example.com",
                "secret123",
                "User One",
                "Bio",
                "ONLINE"
        );
        authService.register(request1);

        RegisterRequest request2 = new RegisterRequest(
                "duplicate@example.com",
                "differentPass",
                "User Two",
                "Bio",
                "ONLINE"
        );

        assertThrows(ConflictException.class, () -> authService.register(request2));
    }

    @Test
    @DisplayName("Should authenticate existing user with valid credentials")
    void testLoginSuccess() {
        // Seeded demo account: rahul@example.com / password123
        AuthRequest loginReq = new AuthRequest("rahul@example.com", "password123");
        AuthResponse response = authService.login(loginReq);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("rahul@example.com", response.getEmail());
    }
}
