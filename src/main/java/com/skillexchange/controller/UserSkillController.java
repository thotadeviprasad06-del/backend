package com.skillexchange.controller;

import com.skillexchange.dto.AddUserSkillRequest;
import com.skillexchange.dto.UserSkillDto;
import com.skillexchange.service.UserSkillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing user skills (offered vs wanted).
 */
@RestController
@RequestMapping("/api/user-skills")
public class UserSkillController {

    @Autowired
    private UserSkillService userSkillService;

    @GetMapping("/me")
    public ResponseEntity<List<UserSkillDto>> getMySkills(Authentication authentication) {
        List<UserSkillDto> skills = userSkillService.getCurrentUserSkills(authentication.getName());
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSkillDto>> getUserSkills(@PathVariable Long userId) {
        List<UserSkillDto> skills = userSkillService.getUserSkillsByUserId(userId);
        return ResponseEntity.ok(skills);
    }

    @PostMapping
    public ResponseEntity<UserSkillDto> addSkill(@Valid @RequestBody AddUserSkillRequest request, Authentication authentication) {
        UserSkillDto added = userSkillService.addUserSkill(authentication.getName(), request);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id, Authentication authentication) {
        userSkillService.deleteUserSkill(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
