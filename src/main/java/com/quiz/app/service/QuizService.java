package com.quiz.app.service;

import com.quiz.app.dto.*;
import com.quiz.app.entity.*;
import com.quiz.app.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuizService {
    
    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuizSessionRepository quizSessionRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    
    public QuizResponseDto startQuiz(QuizRequestDto request) {
        log.info("Starting quiz for topic: {} with {} questions", 
                request.getTopicName(), request.getNumberOfQuestions());
        
        // Find topic
        Topic topic = topicRepository.findByNameIgnoreCase(request.getTopicName())
                .orElseThrow(() -> new RuntimeException(String.format("Please select any other topic. Requested topic: %s is not supported yet.", request.getTopicName())));
        
        // Check if enough questions are available
        Long availableQuestions = questionRepository.countActiveQuestionsByTopicId(topic.getId());
        if (availableQuestions < request.getNumberOfQuestions()) {
            log.error(String.format("Lesser questions available for topic: %d-%s in comparison to the requested questions: %d,", topic.getId(), topic.getName(), request.getNumberOfQuestions()));
        }
        
        // Get random questions
        List<Question> questions = questionRepository.findRandomQuestionsByTopicId(
            topic.getId(), PageRequest.of(0, request.getNumberOfQuestions()));
        
        // Create quiz session
        QuizSession session = new QuizSession();
        session.setSessionId(UUID.randomUUID().toString());
        session.setUserIdentifier(request.getUserIdentifier());
        session.setTopic(topic);
        session.setTotalQuestions(availableQuestions);
        session.setStartedAt(LocalDateTime.now());
        session.setIsCompleted(false);
        
        QuizSession savedSession = quizSessionRepository.save(session);
        
        // Convert questions to DTOs (without correct answers)
        List<QuestionDto> questionDtos = questions.stream()
                .map(this::convertQuestionToDto)
                .collect(Collectors.toList());
        
        return new QuizResponseDto(
            savedSession.getSessionId(),
            topic.getName(),
            request.getNumberOfQuestions(),
            questionDtos
        );
    }
    
    public void submitAnswer(SubmitAnswerDto answerDto) {
        log.info("Submitting answer for session: {}, question: {}", 
                answerDto.getSessionId(), answerDto.getQuestionId());
        
        // Find quiz session
        QuizSession session = quizSessionRepository.findBySessionId(answerDto.getSessionId())
                .orElseThrow(() -> new RuntimeException("Quiz session not found: " + answerDto.getSessionId()));
        
        if (session.getIsCompleted()) {
            throw new RuntimeException("Quiz session is already completed");
        }
        
        // Find question and selected option
        Question question = questionRepository.findById(answerDto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found: " + answerDto.getQuestionId()));
        
        QuestionOption selectedOption = questionOptionRepository.findById(answerDto.getSelectedOptionId())
                .orElseThrow(() -> new RuntimeException("Option not found: " + answerDto.getSelectedOptionId()));
        
        // Check if answer already exists for this question in this session
        Optional<QuizAnswer> existingAnswer = quizAnswerRepository
                .findByQuizSessionIdAndQuestionId(session.getId(), question.getId());
        
        if (existingAnswer.isPresent()) {
            throw new RuntimeException("Answer already submitted for this question in this session");
        }
        
        // Create quiz answer
        QuizAnswer answer = new QuizAnswer();
        answer.setQuizSession(session);
        answer.setQuestion(question);
        answer.setSelectedOption(selectedOption);
        answer.setIsCorrect(selectedOption.getIsCorrect());
        
        quizAnswerRepository.save(answer);
        
        // Update session correct answer count if answer is correct
        if (selectedOption.getIsCorrect()) {
            session.setCorrectAnswers(session.getCorrectAnswers() + 1);
            quizSessionRepository.save(session);
        }
    }
    
    @Transactional(readOnly = true)
    public QuizResultDto getQuizResult(String sessionId) {
        log.info("Getting quiz result for session: {}", sessionId);
        
        QuizSession session = quizSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Quiz session not found: " + sessionId));
        
        List<QuizAnswer> answers = quizAnswerRepository.findByQuizSessionIdOrderByAnsweredAtAsc(session.getId());
        
        // Check if quiz is complete (all questions answered)
        if (answers.size() == session.getTotalQuestions() && !session.getIsCompleted()) {
            session.setIsCompleted(true);
            session.setCompletedAt(LocalDateTime.now());
            quizSessionRepository.save(session);
        }
        
        // Convert answers to answer details
        List<AnswerDetailDto> answerDetails = answers.stream()
                .map(this::convertToAnswerDetailDto)
                .collect(Collectors.toList());
        
        int incorrectAnswers = session.getTotalQuestions() - session.getCorrectAnswers();
        double scorePercentage = (double) session.getCorrectAnswers() / session.getTotalQuestions() * 100;
        
        return new QuizResultDto(
            session.getSessionId(),
            session.getTopic().getName(),
            session.getTotalQuestions(),
            session.getCorrectAnswers(),
            incorrectAnswers,
            scorePercentage,
            session.getStartedAt(),
            session.getCompletedAt(),
            answerDetails
        );
    }
    
    @Transactional(readOnly = true)
    public List<QuizResultDto> getUserQuizHistory(String userIdentifier) {
        log.info("Getting quiz history for user: {}", userIdentifier);
        
        List<QuizSession> sessions = quizSessionRepository.findByUserIdentifierOrderByCreatedAtDesc(userIdentifier);
        
        return sessions.stream()
                .map(session -> {
                    List<QuizAnswer> answers = quizAnswerRepository.findByQuizSessionIdOrderByAnsweredAtAsc(session.getId());
                    List<AnswerDetailDto> answerDetails = answers.stream()
                            .map(this::convertToAnswerDetailDto)
                            .collect(Collectors.toList());
                    
                    int incorrectAnswers = session.getTotalQuestions() - session.getCorrectAnswers();
                    double scorePercentage = (double) session.getCorrectAnswers() / session.getTotalQuestions() * 100;
                    
                    return new QuizResultDto(
                        session.getSessionId(),
                        session.getTopic().getName(),
                        session.getTotalQuestions(),
                        session.getCorrectAnswers(),
                        incorrectAnswers,
                        scorePercentage,
                        session.getStartedAt(),
                        session.getCompletedAt(),
                        answerDetails
                    );
                })
                .collect(Collectors.toList());
    }
    
    private QuestionDto convertQuestionToDto(Question question) {
        List<QuestionOptionDto> optionDtos = question.getOptions().stream()
                .map(option -> new QuestionOptionDto(
                    option.getId(),
                    option.getOptionText(),
                    option.getOptionOrder()
                    // Note: isCorrect is intentionally excluded
                ))
                .collect(Collectors.toList());
        
        return new QuestionDto(
            question.getId(),
            question.getQuestionText(),
            question.getDifficultyLevel(),
            optionDtos
        );
    }
    
    private AnswerDetailDto convertToAnswerDetailDto(QuizAnswer answer) {
        QuestionOption correctOption = questionOptionRepository
                .findCorrectOptionByQuestionId(answer.getQuestion().getId())
                .orElse(null);
        
        return new AnswerDetailDto(
            answer.getQuestion().getId(),
            answer.getQuestion().getQuestionText(),
            answer.getSelectedOption().getOptionText(),
            correctOption != null ? correctOption.getOptionText() : "N/A",
            answer.getIsCorrect()
        );
    }
}