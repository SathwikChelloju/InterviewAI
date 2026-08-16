package com.interview.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.dto.interview.QuestionDto;
import com.interview.dto.interview.QuestionsResponse;
import com.interview.service.GroqService;
import com.interview.service.QuestionGeneratorService;

@Service
public class QuestionGeneratorServiceImpl implements QuestionGeneratorService {

    @Autowired
    private GroqService groqService;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public List<QuestionDto> generateQuestions(
            String technology,
            String interviewType,
            String level,
            Integer questionCount) {


        String prompt = """
                You are a Senior Technical Interviewer at a top product-based company.

                Technology: %s
                Interview Type: %s
                Difficulty: %s
                Total Questions: %d

                Conduct a professional interview.

                Instructions:

                1. Generate EXACTLY %d unique interview questions.

                2. If Interview Type = Coding:

                   - Generate ONLY coding problems.
                   - Do NOT generate theoretical questions.
                   - Each question must be a real programming problem.
                   - Do NOT provide the solution.
                   - Generate exactly 3 sample test cases for every coding question.
                   - Each sample test case must contain:
                       - input
                       - expectedOutput

                3. If Interview Type = Theory:

                   - Generate ONLY conceptual interview questions.
                   - Do NOT generate coding problems.
                   - sampleTestCases must be an empty array.

                4. Cover different topics.

                5. Avoid repeating questions.

                6. Make the questions appropriate for the selected difficulty.

                7. For coding questions, make the input and output
                   realistic and consistent with the problem statement.

                8. Return ONLY valid JSON.

                JSON FORMAT:

                {
                  "questions": [
                    {
                      "number": 1,
                      "question": "Write a Java program to ...",
                      "sampleTestCases": [
                        {
                          "input": "...",
                          "expectedOutput": "..."
                        },
                        {
                          "input": "...",
                          "expectedOutput": "..."
                        },
                        {
                          "input": "...",
                          "expectedOutput": "..."
                        }
                      ]
                    }
                  ]
                }

                IMPORTANT:

                - Return EXACTLY %d questions.
                - Question numbers must start from 1.
                - Question numbers must be sequential.
                - For Coding interviews, every question MUST have exactly 3 sampleTestCases.
                - For Theory interviews, sampleTestCases MUST be [].
                - Do NOT include solutions.
                - Do NOT include correctCode.
                - Do NOT include hidden test cases.
                - Do NOT use markdown.
                - Do NOT use ```json.
                - Do NOT write anything outside the JSON.
                """
                .formatted(
                        technology,
                        interviewType,
                        level,
                        questionCount,
                        questionCount,
                        questionCount
                );


        try {

            String response = groqService.generateResponse(prompt);

            response = response
                    .replaceAll("(?s)^```json\\s*", "")
                    .replaceAll("(?s)^```\\s*", "")
                    .replaceAll("\\s*```$", "")
                    .trim();


            System.out.println("========== QUESTION RESPONSE ==========");
            System.out.println(response);
            System.out.println("=======================================");


            QuestionsResponse questionsResponse =
                    objectMapper.readValue(
                            response,
                            QuestionsResponse.class
                    );


            return questionsResponse.getQuestions();


        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate interview questions",
                    e
            );
        }
    }
}