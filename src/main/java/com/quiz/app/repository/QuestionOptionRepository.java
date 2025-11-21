package com.quiz.app.repository;

import com.quiz.app.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
    
    List<QuestionOption> findByQuestionIdOrderByOptionOrder(Long questionId);
    
    @Query("SELECT qo FROM QuestionOption qo WHERE qo.question.id = :questionId AND qo.isCorrect = true")
    Optional<QuestionOption> findCorrectOptionByQuestionId(@Param("questionId") Long questionId);
    
    List<QuestionOption> findByQuestionIdAndIsCorrectTrue(Long questionId);
}