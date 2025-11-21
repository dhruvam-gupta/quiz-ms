package com.quiz.app.repository;

import com.quiz.app.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    List<Question> findByTopicIdAndIsActiveTrueOrderByIdAsc(Long topicId);
    
    @Query("SELECT q FROM Question q WHERE q.topic.id = :topicId AND q.isActive = true ORDER BY RANDOM()")
    List<Question> findRandomQuestionsByTopicId(@Param("topicId") Long topicId, Pageable pageable);
    
    @Query("SELECT q FROM Question q WHERE q.topic.name = :topicName AND q.isActive = true ORDER BY RANDOM()")
    List<Question> findRandomQuestionsByTopicName(@Param("topicName") String topicName, Pageable pageable);
    
    List<Question> findByTopicNameIgnoreCaseAndIsActiveTrueOrderByIdAsc(String topicName);
    
    @Query("SELECT COUNT(q) FROM Question q WHERE q.topic.id = :topicId AND q.isActive = true")
    Long countActiveQuestionsByTopicId(@Param("topicId") Long topicId);
    
    List<Question> findByTopicIdAndDifficultyLevelAndIsActiveTrueOrderByIdAsc(
        Long topicId, Question.DifficultyLevel difficultyLevel);
}