package com.skillexchange.controller;

import com.skillexchange.dto.CreateSkillRequest;
import com.skillexchange.dto.SkillDto;
import com.skillexchange.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for skill catalog discovery and management.
 */
@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillDto>> getAllSkills(@RequestParam(required = false) String category) {
        List<SkillDto> skills = skillService.getAllSkills(category);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = skillService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDto> getSkillById(@PathVariable Long id) {
        SkillDto skill = skillService.getSkillById(id);
        return ResponseEntity.ok(skill);
    }

    @PostMapping
    public ResponseEntity<SkillDto> createSkill(@Valid @RequestBody CreateSkillRequest request) {
        SkillDto created = skillService.createSkill(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
