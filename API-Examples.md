# Quiz Microservice - API Usage Examples

## 1. Get All Topics
```bash
curl -X GET "http://localhost:8080/api/topics" \
     -H "accept: application/json"
```

**Response:**
```json
{
  "success": true,
  "message": "Topics retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Finance",
      "description": "Questions related to financial concepts, investments, and banking",
      "totalQuestions": 15,
      "isActive": true
    },
    {
      "id": 2,
      "name": "Mathematics",
      "description": "Mathematical problems and concepts",
      "totalQuestions": 20,
      "isActive": true
    }
  ],
  "timestamp": "2023-12-01T10:30:00"
}
```

## 2. Start a Quiz
```bash
curl -X POST "http://localhost:8080/api/quiz/start" \
     -H "accept: application/json" \
     -H "Content-Type: application/json" \
     -d '{
       "topicName": "Finance",
       "numberOfQuestions": 10,
       "userIdentifier": "user123@example.com"
     }'
```

**Response:**
```json
{
  "success": true,
  "message": "Quiz started successfully",
  "data": {
    "sessionId": "550e8400-e29b-41d4-a716-446655440000",
    "topicName": "Finance",
    "totalQuestions": 10,
    "questions": [
      {
        "id": 1,
        "questionText": "What is the primary purpose of diversification in investment portfolios?",
        "difficultyLevel": "MEDIUM",
        "options": [
          {
            "id": 1,
            "optionText": "To reduce risk by spreading investments",
            "optionOrder": 1
          },
          {
            "id": 2,
            "optionText": "To maximize short-term profits",
            "optionOrder": 2
          },
          {
            "id": 3,
            "optionText": "To concentrate investments in one sector",
            "optionOrder": 3
          },
          {
            "id": 4,
            "optionText": "To avoid paying taxes",
            "optionOrder": 4
          }
        ]
      }
    ]
  },
  "timestamp": "2023-12-01T10:35:00"
}
```

## 3. Submit an Answer
```bash
curl -X POST "http://localhost:8080/api/quiz/answer" \
     -H "accept: application/json" \
     -H "Content-Type: application/json" \
     -d '{
       "sessionId": "550e8400-e29b-41d4-a716-446655440000",
       "questionId": 1,
       "selectedOptionId": 1
     }'
```

**Response:**
```json
{
  "success": true,
  "message": "Answer submitted successfully",
  "data": "Answer recorded",
  "timestamp": "2023-12-01T10:36:00"
}
```

## 4. Get Quiz Results
```bash
curl -X GET "http://localhost:8080/api/quiz/result/550e8400-e29b-41d4-a716-446655440000" \
     -H "accept: application/json"
```

**Response:**
```json
{
  "success": true,
  "message": "Quiz result retrieved successfully",
  "data": {
    "sessionId": "550e8400-e29b-41d4-a716-446655440000",
    "topicName": "Finance",
    "totalQuestions": 10,
    "correctAnswers": 8,
    "incorrectAnswers": 2,
    "scorePercentage": 80.0,
    "startedAt": "2023-12-01T10:35:00",
    "completedAt": "2023-12-01T10:45:00",
    "answerDetails": [
      {
        "questionId": 1,
        "questionText": "What is the primary purpose of diversification in investment portfolios?",
        "selectedAnswer": "To reduce risk by spreading investments",
        "correctAnswer": "To reduce risk by spreading investments",
        "isCorrect": true
      }
    ]
  },
  "timestamp": "2023-12-01T10:46:00"
}
```

## 5. Get User Quiz History
```bash
curl -X GET "http://localhost:8080/api/quiz/history/user123@example.com" \
     -H "accept: application/json"
```

## 6. Create New Topic
```bash
curl -X POST "http://localhost:8080/api/topics" \
     -H "accept: application/json" \
     -H "Content-Type: application/json" \
     -d '{
       "name": "Science",
       "description": "General science questions covering physics, chemistry, and biology",
       "isActive": true
     }'
```

## Error Responses

**Validation Error:**
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "topicName": "Topic name is required",
    "numberOfQuestions": "Number of questions must be at least 1"
  },
  "timestamp": "2023-12-01T10:30:00"
}
```

**Business Logic Error:**
```json
{
  "success": false,
  "message": "Topic not found: InvalidTopic",
  "data": null,
  "timestamp": "2023-12-01T10:30:00"
}
```
