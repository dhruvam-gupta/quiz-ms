# Quiz Microservice

A comprehensive Spring Boot microservice for managing quiz topics, questions, and user sessions with REST APIs.

## Features

- **Topic Management**: Create and manage quiz topics (Finance, Mathematics, Physics, etc.)
- **Dynamic Question Loading**: Questions are loaded from database without code changes
- **Session Management**: Track user quiz sessions and progress
- **Scalable Architecture**: Add new quizzes by simply updating database
- **REST APIs**: Complete set of REST endpoints for all operations
- **Database Support**: PostgreSQL for production, H2 for development/testing
- **API Documentation**: Swagger/OpenAPI integration
- **Health Monitoring**: Built-in health checks and monitoring endpoints

## API Endpoints

### Topics
- `GET /api/topics` - Get all active topics
- `GET /api/topics/{id}` - Get topic by ID
- `GET /api/topics/name/{name}` - Get topic by name
- `POST /api/topics` - Create new topic

### Quiz Management
- `POST /api/quiz/start` - Start a new quiz session
- `POST /api/quiz/answer` - Submit an answer
- `GET /api/quiz/result/{sessionId}` - Get quiz results
- `GET /api/quiz/history/{userIdentifier}` - Get user quiz history

### Health Check
- `GET /api/health` - Service health status

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (or use H2 for development)

### Running with Docker
```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f quiz-app
```

### Running Locally
```bash
# Clone the repository
git clone <repository-url>
cd quiz-microservice

# Run with H2 (development)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Run with PostgreSQL (production)
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### API Documentation
Once running, visit:
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs

## Database Schema

The application uses the following main entities:
- **Topics**: Quiz categories (Finance, Math, etc.)
- **Questions**: Individual quiz questions with difficulty levels
- **QuestionOptions**: Multiple choice options (4 per question)
- **QuizSessions**: User quiz attempts and sessions
- **QuizAnswers**: User responses to questions

## Configuration

### Application Properties
Key configuration options in `application.yml`:
```yaml
app:
  quiz:
    default-question-count: 10
    max-question-count: 50
    session-timeout-hours: 24
```

### Profiles
- `dev`: H2 database, debug logging
- `prod`: PostgreSQL, minimal logging
- `docker`: Docker-optimized configuration

## Adding New Quiz Topics

To add a new quiz topic without code changes:

1. Insert topic: `INSERT INTO topics (name, description) VALUES ('New Topic', 'Description');`
2. Add questions: `INSERT INTO questions (question_text, topic_id, difficulty_level) VALUES (...);`
3. Add options: `INSERT INTO question_options (question_id, option_text, is_correct, option_order) VALUES (...);`

## Monitoring

Health check endpoint provides:
- Application status
- Database connectivity
- System metrics

Access via: `GET /api/health`

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Database**: PostgreSQL/H2
- **ORM**: Hibernate/JPA
- **Documentation**: Swagger/OpenAPI
- **Build**: Maven
- **Containerization**: Docker
- **Monitoring**: Spring Actuator
