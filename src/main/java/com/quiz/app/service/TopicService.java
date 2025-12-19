package com.quiz.app.service;

import com.quiz.app.dto.TopicDto;
import com.quiz.app.entity.Topic;
import com.quiz.app.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TopicService {
    
    private final TopicRepository topicRepository;
    
    public List<TopicDto> getActiveTopics(Pageable pageable) {
        log.info("Fetching all active topics");
        List<Topic> topics = topicRepository.findActiveTopics(pageable);
        return topics.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
    }
    
    public Optional<TopicDto> getTopicByName(String name) {
        log.info("Fetching topic by name: {}", name);
        Optional<Topic> topic = topicRepository.findByNameIgnoreCase(name);
        return topic.map(this::convertToDto);
    }
    
    public Optional<TopicDto> getTopicById(Long id) {
        log.info("Fetching topic by ID: {}", id);
        Optional<Topic> topic = topicRepository.findById(id);
        return topic.map(this::convertToDto);
    }
    
    public Long getQuestionCountByTopicId(Long topicId) {
        return topicRepository.countActiveQuestionsByTopicId(topicId);
    }
    
    @Transactional
    public TopicDto createTopic(TopicDto topicDto) {
        log.info("Creating new topic: {}", topicDto.getName());
        Topic topic = new Topic();
        topic.setName(topicDto.getName());
        topic.setDescription(topicDto.getDescription());
        topic.setIsActive(true);
        
        Topic savedTopic = topicRepository.save(topic);
        return convertToDto(savedTopic);
    }
    
    private TopicDto convertToDto(Topic topic) {
        TopicDto dto = new TopicDto();
        dto.setId(topic.getId());
        dto.setName(topic.getName());
        dto.setDescription(topic.getDescription());
        dto.setIsActive(topic.getIsActive());
        
        // Get question count
        Long questionCount = getQuestionCountByTopicId(topic.getId());
        dto.setTotalQuestions(questionCount);
        
        return dto;
    }
}