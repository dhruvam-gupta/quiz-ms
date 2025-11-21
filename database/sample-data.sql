-- Sample Data for Quiz Microservice
-- Insert Topics

INSERT INTO topics (name, description, is_active) VALUES 
('Finance', 'Questions related to financial concepts, investments, and banking', true),
('Mathematics', 'Mathematical problems and concepts', true),
('Physics', 'Physics principles and scientific concepts', true),
('General Knowledge', 'General knowledge questions covering various topics', true),
('Geography', 'World geography, countries, capitals, and landmarks', true),
('History', 'Historical events, dates, and personalities', true),
('Politics', 'Political systems, government structures, and current affairs', true),
('Biology', 'Life sciences, human biology, and ecosystem concepts', true),
('Chemistry', 'Chemical reactions, elements, and laboratory concepts', true),
('Computer Science', 'Programming concepts, algorithms, and technology', true);

-- Insert sample questions for Finance
INSERT INTO questions (question_text, difficulty_level, is_active, topic_id) VALUES 
('What is the primary purpose of diversification in investment portfolios?', 'MEDIUM', true, 
    (SELECT id FROM topics WHERE name = 'Finance')),
('Which of the following is NOT a type of financial market?', 'EASY', true, 
    (SELECT id FROM topics WHERE name = 'Finance')),
('What does P/E ratio stand for in stock analysis?', 'MEDIUM', true, 
    (SELECT id FROM topics WHERE name = 'Finance')),
('In compound interest, what happens to the interest earned?', 'EASY', true, 
    (SELECT id FROM topics WHERE name = 'Finance')),
('What is a mutual fund?', 'EASY', true, 
    (SELECT id FROM topics WHERE name = 'Finance'));

-- Insert sample questions for Mathematics  
INSERT INTO questions (question_text, difficulty_level, is_active, topic_id) VALUES 
('What is the value of π (pi) rounded to 2 decimal places?', 'EASY', true, 
    (SELECT id FROM topics WHERE name = 'Mathematics')),
('What is the derivative of x² + 3x + 2?', 'MEDIUM', true, 
    (SELECT id FROM topics WHERE name = 'Mathematics')),
('In a right triangle, what is the relationship between the sides?', 'MEDIUM', true, 
    (SELECT id FROM topics WHERE name = 'Mathematics')),
('What is 15% of 200?', 'EASY', true, 
    (SELECT id FROM topics WHERE name = 'Mathematics')),
('What is the area of a circle with radius 5?', 'MEDIUM', true, 
    (SELECT id FROM topics WHERE name = 'Mathematics'));

-- Insert options for Finance questions
-- Question 1 options
INSERT INTO question_options (option_text, is_correct, option_order, question_id) VALUES 
('To reduce risk by spreading investments', true, 1, 
    (SELECT id FROM questions WHERE question_text LIKE 'What is the primary purpose of diversification%')),
('To maximize short-term profits', false, 2, 
    (SELECT id FROM questions WHERE question_text LIKE 'What is the primary purpose of diversification%')),
('To concentrate investments in one sector', false, 3, 
    (SELECT id FROM questions WHERE question_text LIKE 'What is the primary purpose of diversification%')),
('To avoid paying taxes', false, 4, 
    (SELECT id FROM questions WHERE question_text LIKE 'What is the primary purpose of diversification%'));

-- Question 2 options
INSERT INTO question_options (option_text, is_correct, option_order, question_id) VALUES 
('Stock Market', false, 1, 
    (SELECT id FROM questions WHERE question_text LIKE 'Which of the following is NOT a type%')),
('Bond Market', false, 2, 
    (SELECT id FROM questions WHERE question_text LIKE 'Which of the following is NOT a type%')),
('Commodity Market', false, 3, 
    (SELECT id FROM questions WHERE question_text LIKE 'Which of the following is NOT a type%')),
('Grocery Market', true, 4, 
    (SELECT id FROM questions WHERE question_text LIKE 'Which of the following is NOT a type%'));

-- Question 3 options
INSERT INTO question_options (option_text, is_correct, option_order, question_id) VALUES 
('Price to Earnings', true, 1, 
    (SELECT id FROM questions WHERE question_text LIKE 'What does P/E ratio stand for%')),
('Profit to Equity', false, 2, 
    (SELECT id FROM questions WHERE question_text LIKE 'What does P/E ratio stand for%')),
('Price to Equity', false, 3, 
    (SELECT id FROM questions WHERE question_text LIKE 'What does P/E ratio stand for%')),
('Profit to Earnings', false, 4, 
    (SELECT id FROM questions WHERE question_text LIKE 'What does P/E ratio stand for%'));

-- Insert options for Mathematics questions
-- Question 1 options (Pi value)
INSERT INTO question_options (option_text, is_correct, option_order, question_id) VALUES 
('3.14', true, 1, 
    (SELECT id FROM questions WHERE question_text LIKE 'What is the value of π%')),
('3.16', false, 2, 
    (SELECT id FROM questions WHERE question_text LIKE 'What is the value of π%')),
('3.12', false, 3, 
    (SELECT id FROM questions WHERE question_text LIKE 'What is the value of π%')),
('3.18', false, 4, 
    (SELECT id FROM questions WHERE question_text LIKE 'What is the value of π%'));

-- Question 2 options (Derivative)
INSERT INTO question_options (option_text, is_correct, option_order, question_id) VALUES 
('2x + 3', true, 1, 
    (SELECT id FROM questions WHERE question_text LIKE 'What is the derivative of x²%')),
('x + 3', false, 2, 
    (SELECT id FROM questions WHERE question_text LIKE 'What is the derivative of x²%')),
('2x² + 3x', false, 3, 
    (SELECT id FROM questions WHERE question_text LIKE 'What is the derivative of x²%')),
('x² + 3', false, 4, 
    (SELECT id FROM questions WHERE question_text LIKE 'What is the derivative of x²%'));