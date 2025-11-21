package com.quiz.app.repository;

import com.quiz.app.entity.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {
    
    List<QuizAnswer> findByQuizSessionIdOrderByAnsweredAtAsc(Long quizSessionId);
    
    Optional<QuizAnswer> findByQuizSessionIdAndQuestionId(Long quizSessionId, Long questionId);
    
    @Query("SELECT COUNT(qa) FROM QuizAnswer qa WHERE qa.quizSession.id = :sessionId AND qa.isCorrect = true")
    Long countCorrectAnswersBySessionId(@Param("sessionId") Long sessionId);
    
    List<QuizAnswer> findByQuizSessionIdAndIsCorrectTrue(Long quizSessionId);
    
    List<QuizAnswer> findByQuizSessionIdAndIsCorrectFalse(Long quizSessionId);
}