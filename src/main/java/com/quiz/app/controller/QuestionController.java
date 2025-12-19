package com.quiz.app.controller;

import com.quiz.app.dto.ApiResponse;
import com.quiz.app.dto.QuestionDto;
import com.quiz.app.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Question Management", description = "APIs for managing quiz questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    @Operation(summary = "Get all active topics", description = "Retrieve all active quiz topics with question counts")
    public ResponseEntity<ApiResponse<List<QuestionDto>>> getActiveQuestions(Pageable pageable) {
        try {
            List<QuestionDto> questions = topicService.getActiveQuestions(pageable);
            return ResponseEntity.ok(ApiResponse.success("Questions retrieved successfully", questions));
        } catch (Exception e) {
            log.error("Error retrieving topics", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to retrieve questions: " + e.getMessage()));
        }
    }
}