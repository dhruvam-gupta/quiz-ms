package com.quiz.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDetailDto {
    private Long questionId;
    private String questionText;
    private String selectedAnswer;
    private String correctAnswer;
    private Boolean isCorrect;
}