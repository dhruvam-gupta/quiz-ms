package com.quiz.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizRequestDto {
    
    @NotBlank(message = "Topic name is required")
    private String topicName;
    
    @Min(value = 1, message = "Number of questions must be at least 1")
    @Max(value = 20, message = "Number of questions cannot exceed 20")
    private Integer numberOfQuestions = 10;
    
    private String userIdentifier; // Optional for tracking user sessions
}