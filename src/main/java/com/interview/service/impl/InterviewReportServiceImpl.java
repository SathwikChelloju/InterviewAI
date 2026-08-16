package com.interview.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.dto.interview.InterviewReport;
import com.interview.dto.interview.QuestionEvaluationDto;
import com.interview.entity.InterviewAnswer;
import com.interview.repository.InterviewAnswerRepository;
import com.interview.service.GroqService;
import com.interview.service.InterviewReportService;

import com.interview.entity.InterviewSession;
import com.interview.repository.InterviewSessionRepository;

@Service
public class InterviewReportServiceImpl implements InterviewReportService {

    @Autowired
    private InterviewAnswerRepository answerRepository;

    @Autowired
    private GroqService groqService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InterviewSessionRepository sessionRepository;
    
    @Override
    public InterviewReport generateReport(Long interviewId) {

        // ==========================================
        // 1. GET INTERVIEW SESSION
        // ==========================================

        InterviewSession session =
                sessionRepository.findById(interviewId)
                        .orElseThrow(() ->
                                new RuntimeException("Interview not found"));


        // ==========================================
        // 2. GET ALL ANSWERS
        // ==========================================

        List<InterviewAnswer> answers =
                answerRepository
                        .findByInterviewSessionIdOrderByQuestionNumber(interviewId);


        // ==========================================
        // 3. CHECK INTERVIEW COMPLETION
        // ==========================================

        if (answers.size() != session.getQuestionCount()) {

            throw new RuntimeException(
                    "Interview is incomplete."
            );
        }


        for (InterviewAnswer answer : answers) {

            if (answer.getAnswer() == null ||
                    answer.getAnswer().isBlank()) {

                throw new RuntimeException(
                        "Interview is incomplete."
                );
            }
        }


        // ==========================================
        // 4. BUILD AI REPORT PROMPT
        // ==========================================

        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                You are a Senior Technical Interviewer.

                Below are the interview questions, candidate answers,
                AI feedback and scores.

                Generate a detailed interview performance report.

                """);


        for (InterviewAnswer answer : answers) {

            prompt.append("""
                    
                    Question:
                    """)
                    .append(answer.getQuestion())
                    .append("\n\n");


            prompt.append("""
                    Candidate Answer:
                    """)
                    .append(answer.getAnswer())
                    .append("\n\n");


            prompt.append("""
                    Feedback:
                    """)
                    .append(answer.getFeedback())
                    .append("\n\n");


            prompt.append("""
                    Score:
                    """)
                    .append(answer.getScore())
                    .append("\n\n");
        }


        prompt.append("""
                
                Generate a complete interview report.

                Return ONLY valid JSON.

                {
                  "overallScore":0,
                  "percentage":0,
                  "technicalKnowledge":0,
                  "communication":0,
                  "problemSolving":0,
                  "strengths":[
                    ""
                  ],
                  "weaknesses":[
                    ""
                  ],
                  "recommendation":""
                }

                Rules:

                - overallScore must be between 0 and 100.
                - percentage must be between 0 and 100.
                - technicalKnowledge must be between 0 and 100.
                - communication must be between 0 and 100.
                - problemSolving must be between 0 and 100.
                - strengths must contain meaningful observations.
                - weaknesses must contain meaningful observations.
                - recommendation must be useful to the candidate.
                - Return ONLY JSON.
                - Do not use markdown.
                - Do not explain anything outside the JSON.

                """);


        // ==========================================
        // 5. CALL GROQ
        // ==========================================

        try {

            String response =
                    groqService.generateResponse(prompt.toString());


            response = response
                    .replaceAll("(?s)^```json\\s*", "")
                    .replaceAll("(?s)^```\\s*", "")
                    .replaceAll("\\s*```$", "")
                    .trim();


            System.out.println(
                    "========== INTERVIEW REPORT AI RESPONSE =========="
            );

            System.out.println(response);

            System.out.println(
                    "==================================================="
            );


            // ==========================================
            // 6. CONVERT AI RESPONSE TO DTO
            // ==========================================

            InterviewReport report =
                    objectMapper.readValue(
                            response,
                            InterviewReport.class
                    );


            // ==========================================
            // 7. SAVE SCORE TO SESSION
            // ==========================================

            session.setOverallScore(
                    report.getOverallScore()
            );

            session.setPercentage(
                    report.getPercentage()
            );

            sessionRepository.save(session);


            // ==========================================
            // 8. ADD INTERVIEW INFORMATION
            // ==========================================

            report.setInterviewId(
                    session.getId()
            );

            report.setTechnology(
                    session.getTechnology()
            );

            report.setInterviewType(
                    session.getInterviewType()
            );

            report.setLevel(
                    session.getLevel()
            );

            report.setQuestionCount(
                    session.getQuestionCount()
            );


            // ==========================================
            // 9. BUILD QUESTION-WISE REPORT
            // ==========================================

            List<QuestionEvaluationDto> questionReports =
                    new java.util.ArrayList<>();


            for (InterviewAnswer answer : answers) {

                QuestionEvaluationDto questionReport =
                        new QuestionEvaluationDto();


                questionReport.setQuestionNumber(
                        answer.getQuestionNumber()
                );


                questionReport.setQuestion(
                        answer.getQuestion()
                );


                questionReport.setAnswer(
                        answer.getAnswer()
                );


                questionReport.setScore(
                        answer.getScore()
                );


                questionReport.setFeedback(
                        answer.getFeedback()
                );


                questionReport.setIdealAnswer(
                        answer.getIdealAnswer()
                );


//                questionReport.setCorrectCode(
//                        answer.getCorrectCode()
//                );
//
//
//                questionReport.setCodeExplanation(
//                        answer.getCodeExplanation()
//                );
//
//
//                questionReport.setTimeComplexity(
//                        answer.getTimeComplexity()
//                );
//
//
//                questionReport.setSpaceComplexity(
//                        answer.getSpaceComplexity()
//                );


                questionReports.add(
                        questionReport
                );
            }


            report.setQuestions(
                    questionReports
            );


            // ==========================================
            // 10. RETURN COMPLETE REPORT
            // ==========================================

            return report;


        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Failed to generate interview report",
                    e
            );
        }
    }
}