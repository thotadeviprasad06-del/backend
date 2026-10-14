package com.skillexchange.repository;

import com.skillexchange.entity.KnowledgeDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for KnowledgeDoc entity used by RAG.
 */
@Repository
public interface KnowledgeDocRepository extends JpaRepository<KnowledgeDoc, Long> {

    List<KnowledgeDoc> findByCategoryIgnoreCase(String category);

    @Query("SELECT k FROM KnowledgeDoc k WHERE " +
           "LOWER(k.title) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(k.content) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(k.keywords) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(k.topic) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<KnowledgeDoc> searchByTerm(@Param("term") String term);
}
