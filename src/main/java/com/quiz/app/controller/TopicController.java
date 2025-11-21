package com.quiz.app.controller;

import com.quiz.app.dto.ApiResponse;
import com.quiz.app.dto.TopicDto;
import com.quiz.app.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Topic Management", description = "APIs for managing quiz topics")
public class TopicController {
    
    private final TopicService topicService;
    
    @GetMapping
    @Operation(summary = "Get all active topics", description = "Retrieve all active quiz topics with question counts")
    public ResponseEntity<ApiResponse<List<TopicDto>>> getAllTopics() {
        try {
            List<TopicDto> topics = topicService.getAllActiveTopics();
            return ResponseEntity.ok(ApiResponse.success("Topics retrieved successfully", topics));
        } catch (Exception e) {
            log.error("Error retrieving topics", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to retrieve topics: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get topic by ID", description = "Retrieve a specific topic by its ID")
    public ResponseEntity<ApiResponse<TopicDto>> getTopicById(@PathVariable Long id) {
        try {
            Optional<TopicDto> topic = topicService.getTopicById(id);
            if (topic.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Topic retrieved successfully", topic.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error retrieving topic by ID: {}", id, e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to retrieve topic: " + e.getMessage()));
        }
    }
    
    @GetMapping("/name/{name}")
    @Operation(summary = "Get topic by name", description = "Retrieve a specific topic by its name")
    public ResponseEntity<ApiResponse<TopicDto>> getTopicByName(@PathVariable String name) {
        try {
            Optional<TopicDto> topic = topicService.getTopicByName(name);
            if (topic.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Topic retrieved successfully", topic.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error retrieving topic by name: {}", name, e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to retrieve topic: " + e.getMessage()));
        }
    }
    
    @PostMapping
    @Operation(summary = "Create new topic", description = "Create a new quiz topic")
    public ResponseEntity<ApiResponse<TopicDto>> createTopic(@Valid @RequestBody TopicDto topicDto) {
        try {
            TopicDto createdTopic = topicService.createTopic(topicDto);
            return ResponseEntity.ok(ApiResponse.success("Topic created successfully", createdTopic));
        } catch (Exception e) {
            log.error("Error creating topic", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create topic: " + e.getMessage()));
        }
    }
}