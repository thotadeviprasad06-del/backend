package com.skillexchange.service.impl;

import com.skillexchange.dto.CreateSkillRequest;
import com.skillexchange.dto.SkillDto;
import com.skillexchange.entity.Skill;
import com.skillexchange.exception.ConflictException;
import com.skillexchange.exception.ResourceNotFoundException;
import com.skillexchange.repository.SkillRepository;
import com.skillexchange.service.SkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of SkillService featuring Spring Cache for category lists.
 */
@Service
public class SkillServiceImpl implements SkillService {

    private static final Logger logger = LoggerFactory.getLogger(SkillServiceImpl.class);

    @Autowired
    private SkillRepository skillRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SkillDto> getAllSkills(String category) {
        List<Skill> skills;
        if (category != null && !category.trim().isEmpty()) {
            skills = skillRepository.findByCategoryIgnoreCaseAndIsActiveTrueOrderByNameAsc(category.trim());
        } else {
            skills = skillRepository.findByIsActiveTrueOrderByNameAsc();
        }

        return skills.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Caches distinct skill categories in memory to improve query performance on read-heavy dropdowns.
     * Fulfills the Full Stack Java syllabus caching requirement.
     */
    @Override
    @Cacheable(value = "skillCategories")
    @Transactional(readOnly = true)
    public List<String> getCategories() {
        logger.info("[CACHE MISS] Fetching distinct skill categories directly from database");
        return skillRepository.findDistinctCategories();
    }

    @Override
    @CacheEvict(value = "skillCategories", allEntries = true)
    @Transactional
    public SkillDto createSkill(CreateSkillRequest request) {
        String name = request.getName().trim();
        if (skillRepository.existsByNameIgnoreCase(name)) {
            throw new ConflictException("Skill already exists in catalog: " + name);
        }

        Skill skill = new Skill();
        skill.setName(name);
        skill.setCategory(request.getCategory().trim());
        skill.setDescription(request.getDescription());
        skill.setActive(true);

        Skill saved = skillRepository.save(skill);
        logger.info("[CACHE EVICT] Created new skill '{}'; invalidated skillCategories cache", name);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SkillDto getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
        return mapToDto(skill);
    }

    private SkillDto mapToDto(Skill skill) {
        return new SkillDto(
                skill.getId(),
                skill.getName(),
                skill.getCategory(),
                skill.getDescription(),
                skill.isActive(),
                skill.getCreatedAt()
        );
    }
}
