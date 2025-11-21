package com.quiz.app.repository;

import com.quiz.app.entity.QuizSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizSessionRepository extends JpaRepository<QuizSession, Long> {
    
    Optional<QuizSession> findBySessionId(String sessionId);
    
    List<QuizSession> findByUserIdentifierOrderByCreatedAtDesc(String userIdentifier);
    
    List<QuizSession> findByTopicIdAndIsCompletedTrueOrderByCreatedAtDesc(Long topicId);
    
    @Query("SELECT qs FROM QuizSession qs WHERE qs.userIdentifier = :userIdentifier AND " +
           "qs.isCompleted = false AND qs.createdAt > :cutoffTime")
    List<QuizSession> findActiveSessionsByUser(@Param("userIdentifier") String userIdentifier, 
                                               @Param("cutoffTime") LocalDateTime cutoffTime);
    
    @Query("SELECT AVG(CAST(qs.correctAnswers AS DOUBLE) / qs.totalQuestions * 100) FROM QuizSession qs " +
           "WHERE qs.topic.id = :topicId AND qs.isCompleted = true")
    Double getAverageScoreByTopicId(@Param("topicId") Long topicId);
}