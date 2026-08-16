package com.interview.service;

import java.util.List;

import com.interview.dto.interview.QuestionDto;

public interface QuestionGeneratorService {

	List<QuestionDto> generateQuestions(
	        String technology,
	        String interviewType,
	        String level,
	        Integer questionCount);

}