package com.skillexchange.repository;

import com.skillexchange.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for UserSkill entity.
 */
@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findByUserId(Long userId);

    List<UserSkill> findByUserIdAndSkillType(Long userId, String skillType);

    Optional<UserSkill> findByUserIdAndSkillIdAndSkillType(Long userId, Long skillId, String skillType);

    boolean existsByUserIdAndSkillIdAndSkillType(Long userId, Long skillId, String skillType);

    List<UserSkill> findBySkillType(String skillType);

    List<UserSkill> findBySkillIdAndSkillType(Long skillId, String skillType);

    long countByUserIdAndSkillType(Long userId, String skillType);

    void deleteByUserIdAndSkillId(Long userId, Long skillId);

    @Query("SELECT us FROM UserSkill us JOIN FETCH us.user u JOIN FETCH us.skill s " +
           "WHERE us.skillType = :skillType AND u.isActive = true AND u.id != :excludeUserId")
    List<UserSkill> findAllActiveOtherUserSkills(@Param("skillType") String skillType, @Param("excludeUserId") Long excludeUserId);
}
