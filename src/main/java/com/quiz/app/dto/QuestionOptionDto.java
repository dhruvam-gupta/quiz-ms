package com.quiz.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionDto {
    private Long id;
    private String optionText;
    private Integer optionOrder;
}