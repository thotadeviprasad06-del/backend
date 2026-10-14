package com.skillexchange.service;

import com.skillexchange.dto.CreateSkillRequest;
import com.skillexchange.dto.SkillDto;

import java.util.List;

/**
 * Service interface for platform skill catalog management.
 */
public interface SkillService {

    List<SkillDto> getAllSkills(String category);

    List<String> getCategories();

    SkillDto createSkill(CreateSkillRequest request);

    SkillDto getSkillById(Long id);
}
