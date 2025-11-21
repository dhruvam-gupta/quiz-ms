package com.quiz.app.dto;

import com.quiz.app.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Long id;
    private String questionText;
    private Question.DifficultyLevel difficultyLevel;
    private List<QuestionOptionDto> options;
}