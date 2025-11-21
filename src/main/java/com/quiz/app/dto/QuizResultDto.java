package com.quiz.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultDto {
    private String sessionId;
    private String topicName;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer incorrectAnswers;
    private Double scorePercentage;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private List<AnswerDetailDto> answerDetails;
}