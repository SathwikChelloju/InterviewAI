package com.interview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interview.entity.InterviewAnswer;

public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {

	List<InterviewAnswer> findByInterviewSessionIdOrderByQuestionNumber(Long interviewId);

	InterviewAnswer findByInterviewSessionIdAndQuestionNumber(
	        Long interviewId,
	        Integer questionNumber);
}