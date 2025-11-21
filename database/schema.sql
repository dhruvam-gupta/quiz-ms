-- Quiz Microservice Database Schema
-- PostgreSQL/H2 Compatible

-- Create Topics table
CREATE TABLE IF NOT EXISTS topics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on topics name
CREATE INDEX IF NOT EXISTS idx_topics_name ON topics(name);
CREATE INDEX IF NOT EXISTS idx_topics_active ON topics(is_active);

-- Create Questions table
CREATE TABLE IF NOT EXISTS questions (
    id BIGSERIAL PRIMARY KEY,
    question_text TEXT NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    topic_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_questions_topic FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE
);

-- Create indexes on questions
CREATE INDEX IF NOT EXISTS idx_questions_topic_id ON questions(topic_id);
CREATE INDEX IF NOT EXISTS idx_questions_active ON questions(is_active);
CREATE INDEX IF NOT EXISTS idx_questions_difficulty ON questions(difficulty_level);

-- Create Question Options table
CREATE TABLE IF NOT EXISTS question_options (
    id BIGSERIAL PRIMARY KEY,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    option_order INTEGER,
    question_id BIGINT NOT NULL,
    CONSTRAINT fk_options_question FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

-- Create indexes on question_options
CREATE INDEX IF NOT EXISTS idx_options_question_id ON question_options(question_id);
CREATE INDEX IF NOT EXISTS idx_options_correct ON question_options(is_correct);

-- Create Quiz Sessions table
CREATE TABLE IF NOT EXISTS quiz_sessions (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) NOT NULL UNIQUE,
    user_identifier VARCHAR(255),
    topic_id BIGINT NOT NULL,
    total_questions INTEGER NOT NULL,
    correct_answers INTEGER NOT NULL DEFAULT 0,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sessions_topic FOREIGN KEY (topic_id) REFERENCES topics(id)
);

-- Create indexes on quiz_sessions
CREATE INDEX IF NOT EXISTS idx_sessions_session_id ON quiz_sessions(session_id);
CREATE INDEX IF NOT EXISTS idx_sessions_user ON quiz_sessions(user_identifier);
CREATE INDEX IF NOT EXISTS idx_sessions_topic_id ON quiz_sessions(topic_id);
CREATE INDEX IF NOT EXISTS idx_sessions_completed ON quiz_sessions(is_completed);

-- Create Quiz Answers table
CREATE TABLE IF NOT EXISTS quiz_answers (
    id BIGSERIAL PRIMARY KEY,
    quiz_session_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option_id BIGINT NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_answers_session FOREIGN KEY (quiz_session_id) REFERENCES quiz_sessions(id) ON DELETE CASCADE,
    CONSTRAINT fk_answers_question FOREIGN KEY (question_id) REFERENCES questions(id),
    CONSTRAINT fk_answers_option FOREIGN KEY (selected_option_id) REFERENCES question_options(id),
    CONSTRAINT uk_session_question UNIQUE (quiz_session_id, question_id)
);

-- Create indexes on quiz_answers
CREATE INDEX IF NOT EXISTS idx_answers_session_id ON quiz_answers(quiz_session_id);
CREATE INDEX IF NOT EXISTS idx_answers_question_id ON quiz_answers(question_id);
CREATE INDEX IF NOT EXISTS idx_answers_correct ON quiz_answers(is_correct);