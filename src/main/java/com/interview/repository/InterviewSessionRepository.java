package com.interview.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.interview.entity.InterviewSession;

public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {

	Optional<InterviewSession> findFirstByUserIdAndCompletedFalseOrderByStartedAtDesc(Long userId);
	List<InterviewSession> findByUserIdOrderByStartedAtDesc(Long userId);
}