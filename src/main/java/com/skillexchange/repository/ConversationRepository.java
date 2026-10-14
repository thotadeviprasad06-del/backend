package com.skillexchange.repository;

import com.skillexchange.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Conversation entity.
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByExchangeRequestId(Long exchangeRequestId);

    @Query("SELECT c FROM Conversation c JOIN FETCH c.exchangeRequest er " +
           "WHERE er.requester.id = :userId OR er.receiver.id = :userId " +
           "ORDER BY c.updatedAt DESC")
    List<Conversation> findUserConversations(@Param("userId") Long userId);
}
