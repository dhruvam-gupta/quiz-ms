package com.quiz.app.repository;

import com.quiz.app.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    
    Optional<Topic> findByNameIgnoreCase(String name);
    
    List<Topic> findByIsActiveTrueOrderByName();
    
    @Query("SELECT t FROM Topic t WHERE t.isActive = true AND " +
           "EXISTS (SELECT 1 FROM Question q WHERE q.topic = t AND q.isActive = true)")
    List<Topic> findActiveTopics(Pageable pageable);
}