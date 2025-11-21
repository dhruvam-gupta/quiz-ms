package com.quiz.app.controller;

import com.quiz.app.dto.*;
import com.quiz.app.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Quiz Management", description = "APIs for managing quiz sessions and answers")
public class QuizController {
    
    private final QuizService quizService;
    
    @PostMapping("/start")
    @Operation(summary = "Start a new quiz", description = "Start a new quiz session for a specific topic")
    public ResponseEntity<ApiResponse<QuizResponseDto>> startQuiz(@Valid @RequestBody QuizRequestDto request) {
        try {
            QuizResponseDto quiz = quizService.startQuiz(request);
            return ResponseEntity.ok(ApiResponse.success("Quiz started successfully", quiz));
        } catch (Exception e) {
            log.error("Error starting quiz for topic: {}", request.getTopicName(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to start quiz: " + e.getMessage()));
        }
    }
    
    @PostMapping("/answer")
    @Operation(summary = "Submit quiz answer", description = "Submit an answer for a quiz question")
    public ResponseEntity<ApiResponse<String>> submitAnswer(@Valid @RequestBody SubmitAnswerDto answerDto) {
        try {
            quizService.submitAnswer(answerDto);
            return ResponseEntity.ok(ApiResponse.success("Answer submitted successfully", "Answer recorded"));
        } catch (Exception e) {
            log.error("Error submitting answer for session: {}", answerDto.getSessionId(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to submit answer: " + e.getMessage()));
        }
    }
    
    @GetMapping("/result/{sessionId}")
    @Operation(summary = "Get quiz results", description = "Get the results of a completed or ongoing quiz session")
    public ResponseEntity<ApiResponse<QuizResultDto>> getQuizResult(@PathVariable String sessionId) {
        try {
            QuizResultDto result = quizService.getQuizResult(sessionId);
            return ResponseEntity.ok(ApiResponse.success("Quiz result retrieved successfully", result));
        } catch (Exception e) {
            log.error("Error retrieving quiz result for session: {}", sessionId, e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve quiz result: " + e.getMessage()));
        }
    }
    
    @GetMapping("/history/{userIdentifier}")
    @Operation(summary = "Get user quiz history", description = "Get the quiz history for a specific user")
    public ResponseEntity<ApiResponse<List<QuizResultDto>>> getUserQuizHistory(@PathVariable String userIdentifier) {
        try {
            List<QuizResultDto> history = quizService.getUserQuizHistory(userIdentifier);
            return ResponseEntity.ok(ApiResponse.success("Quiz history retrieved successfully", history));
        } catch (Exception e) {
            log.error("Error retrieving quiz history for user: {}", userIdentifier, e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to retrieve quiz history: " + e.getMessage()));
        }
    }
}