package com.quiz.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class QuizApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);
    }
}