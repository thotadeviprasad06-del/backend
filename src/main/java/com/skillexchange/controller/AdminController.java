package com.skillexchange.controller;

import com.skillexchange.dto.SkillDto;
import com.skillexchange.dto.UserProfileDto;
import com.skillexchange.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for administrator moderation actions.
 * Protected with @PreAuthorize("hasAuthority('ROLE_ADMIN')").
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserProfileDto>> getAllUsers() {
        List<UserProfileDto> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<UserProfileDto> toggleUserStatus(@PathVariable Long id) {
        UserProfileDto updated = adminService.toggleUserStatus(id);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/skills")
    public ResponseEntity<List<SkillDto>> getAllSkillsAdmin() {
        List<SkillDto> skills = adminService.getAllSkillsAdmin();
        return ResponseEntity.ok(skills);
    }

    @PatchMapping("/skills/{id}/status")
    public ResponseEntity<SkillDto> toggleSkillStatus(@PathVariable Long id) {
        SkillDto updated = adminService.toggleSkillStatus(id);
        return ResponseEntity.ok(updated);
    }
}
