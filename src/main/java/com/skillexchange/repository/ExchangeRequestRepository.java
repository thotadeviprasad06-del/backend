package com.skillexchange.repository;

import com.skillexchange.entity.ExchangeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for ExchangeRequest entity.
 */
@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {

    List<ExchangeRequest> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    List<ExchangeRequest> findByRequesterIdOrderByCreatedAtDesc(Long requesterId);

    @Query("SELECT er FROM ExchangeRequest er WHERE er.requester.id = :userId OR er.receiver.id = :userId ORDER BY er.updatedAt DESC")
    List<ExchangeRequest> findAllUserExchanges(@Param("userId") Long userId);

    long countByStatus(String status);

    long countByReceiverIdAndStatus(Long receiverId, String status);

    long countByRequesterIdAndStatus(Long requesterId, String status);

    @Query("SELECT COUNT(er) FROM ExchangeRequest er WHERE (er.requester.id = :userId OR er.receiver.id = :userId) AND er.status = :status")
    long countUserExchangesByStatus(@Param("userId") Long userId, @Param("status") String status);

    boolean existsByRequesterIdAndReceiverIdAndOfferedSkillIdAndWantedSkillIdAndStatus(
            Long requesterId, Long receiverId, Long offeredSkillId, Long wantedSkillId, String status);

    Page<ExchangeRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
