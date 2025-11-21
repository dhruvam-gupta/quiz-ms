package com.quiz.app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.app.QuizApplication;
import com.quiz.app.dto.QuizRequestDto;
import com.quiz.app.entity.Question;
import com.quiz.app.entity.QuestionOption;
import com.quiz.app.entity.Topic;
import com.quiz.app.repository.QuestionOptionRepository;
import com.quiz.app.repository.QuestionRepository;
import com.quiz.app.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = QuizApplication.class)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
public class QuizIntegrationTest {
    
    @Autowired
    private WebApplicationContext context;
    
    @Autowired
    private TopicRepository topicRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuestionOptionRepository questionOptionRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        setupTestData();
    }
    
    private void setupTestData() {
        // Create test topic
        Topic topic = new Topic();
        topic.setName("Test Topic");
        topic.setDescription("Test Description");
        topic.setIsActive(true);
        topic = topicRepository.save(topic);
        
        // Create test question
        Question question = new Question();
        question.setQuestionText("What is 2 + 2?");
        question.setTopic(topic);
        question.setIsActive(true);
        question.setDifficultyLevel(Question.DifficultyLevel.EASY);
        question = questionRepository.save(question);
        
        // Create test options
        QuestionOption option1 = new QuestionOption();
        option1.setOptionText("3");
        option1.setIsCorrect(false);
        option1.setOptionOrder(1);
        option1.setQuestion(question);
        questionOptionRepository.save(option1);
        
        QuestionOption option2 = new QuestionOption();
        option2.setOptionText("4");
        option2.setIsCorrect(true);
        option2.setOptionOrder(2);
        option2.setQuestion(question);
        questionOptionRepository.save(option2);
    }
    
    @Test
    public void testGetAllTopics() throws Exception {
        mockMvc.perform(get("/api/topics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("Test Topic"));
    }
    
    @Test
    public void testStartQuiz() throws Exception {
        QuizRequestDto request = new QuizRequestDto();
        request.setTopicName("Test Topic");
        request.setNumberOfQuestions(1);
        request.setUserIdentifier("testuser");
        
        mockMvc.perform(post("/api/quiz/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.topicName").value("Test Topic"))
                .andExpect(jsonPath("$.data.totalQuestions").value(1));
    }
}