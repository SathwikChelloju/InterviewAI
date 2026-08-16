package com.interview.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.dto.interview.AnswerEvaluation;
import com.interview.service.AnswerEvaluationService;
import com.interview.service.GroqService;

@Service
public class AnswerEvaluationServiceImpl implements AnswerEvaluationService {

    @Autowired
    private GroqService groqService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public AnswerEvaluation evaluate(
            String question,
            String answer,
            String interviewType,
            String technology) {
    	
    	
    	String prompt;

    	if ("Coding".equalsIgnoreCase(interviewType)) {

    		prompt = """
    				You are a Senior Software Engineer conducting a live coding interview.

    				Programming Language:
    				%s

    				Problem:
    				%s

    				Candidate Code:
    				%s

    				Your tasks:

    				1. Evaluate the candidate's solution.
    				2. Detect syntax errors, logical errors, edge cases and inefficiencies.
    				3. Give a score out of 10.
    				4. Provide constructive feedback.
    				5. Generate the best interview-ready solution in the same programming language.
    				6. Explain the solution.
    				7. Mention the Time Complexity.
    				8. Mention the Space Complexity.

    				Return ONLY valid JSON.

					{
					  "score":0,
					  "feedback":"",
					  "correctCode":"",
					  "codeExplanation":"",
					  "timeComplexity":"",
					  "spaceComplexity":"",
					  "testCases":[
					    {
					      "input":"",
					      "expectedOutput":"",
					      "candidatePass":true
					    }
					  ]
					}

    				IMPORTANT:
    				- Return complete executable code in %s.
					- The value of "correctCode" MUST be a valid JSON string.
					- Escape every newline as \\n.
					- Escape every double quote inside the code as \\\".
					- Never return raw multi-line code.
    				- Follow %s coding standards and best practices.
    				- Use meaningful variable names.
    				- Use the most optimal approach.
    				- Handle edge cases.
    				- Do NOT use markdown.
    				- Do NOT write anything outside the JSON.
    				9. Generate 3 to 5 meaningful test cases.
					10. For each test case provide:
					    - input
					    - expectedOutput
					    - whether the candidate's solution would pass it.
					
					IMPORTANT:
					- Never omit the testCases field.
					- Always return between 3 and 5 test cases.
					- candidatePass must be either true or false.
					    				"""
    				.formatted(
    				        technology,
    				        question,
    				        answer,
    				        technology,
    				        technology
    				);
    	} else {

    	    prompt = """
    	    		You are a Senior Technical Interviewer at a top product-based company.

    			Evaluate the candidate's interview answer.

    			Question:
    			%s

    			Candidate Answer:
    			%s

    			Your task:

    			1. Give a score out of 10.
    			2. Explain what the candidate did well.
    			3. Explain what the candidate missed.
    			4. Generate an Ideal Answer that would impress an interviewer.
    			5. The Ideal Answer should be technically accurate, concise, professional, and interview-ready.

    			Return ONLY valid JSON.

    			{
    			  "score":0,
    			  "feedback":"",
    			  "idealAnswer":""
    			}

    			Do NOT use markdown.
    			Do NOT explain anything outside the JSON.
    	    ...
    	    """.formatted(question, answer);

    	}

    	
        try {

            String response = groqService.generateResponse(prompt);

            response = response.replaceAll("(?s)^```json\\s*", "")
                    .replaceAll("(?s)^```\\s*", "")
                    .replaceAll("\\s*```$", "")
                    .trim();
            System.out.println("========== AI RESPONSE ==========");
            System.out.println(response);
            System.out.println("=================================");
            
            return objectMapper.readValue(response, AnswerEvaluation.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to evaluate answer", e);
        }
    }
}