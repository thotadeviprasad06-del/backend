package com.skillexchange.controller;

import com.skillexchange.dto.UpdateProfileRequest;
import com.skillexchange.dto.UserProfileDto;
import com.skillexchange.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for user profile operations and peer discovery.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/me")
    public ResponseEntity<UserProfileDto> getMyProfile(Authentication authentication) {
        UserProfileDto profile = userService.getCurrentUserProfile(authentication.getName());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/users/me")
    public ResponseEntity<UserProfileDto> updateMyProfile(@Valid @RequestBody UpdateProfileRequest request,
                                                          Authentication authentication) {
        UserProfileDto updated = userService.updateUserProfile(authentication.getName(), request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserProfileDto> getUserPublicProfile(@PathVariable Long id) {
        UserProfileDto profile = userService.getUserPublicProfile(id);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/discovery/users")
    public ResponseEntity<List<UserProfileDto>> searchUsers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String proficiency,
            Authentication authentication) {
        List<UserProfileDto> results = userService.searchUsers(query, category, proficiency, authentication.getName());
        return ResponseEntity.ok(results);
    }
}
