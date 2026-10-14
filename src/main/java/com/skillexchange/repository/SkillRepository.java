package com.skillexchange.repository;

import com.skillexchange.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Skill entity.
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Skill> findByIsActiveTrueOrderByNameAsc();

    List<Skill> findByCategoryIgnoreCaseAndIsActiveTrueOrderByNameAsc(String category);

    Page<Skill> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT DISTINCT s.category FROM Skill s WHERE s.isActive = true ORDER BY s.category ASC")
    List<String> findDistinctCategories();

    List<Skill> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
