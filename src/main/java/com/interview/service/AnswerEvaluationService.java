package com.interview.service;

import com.interview.dto.interview.AnswerEvaluation;

public interface AnswerEvaluationService {

	AnswerEvaluation evaluate(
	        String question,
	        String answer,
	        String interviewType,
	        String technology
	);
	
}