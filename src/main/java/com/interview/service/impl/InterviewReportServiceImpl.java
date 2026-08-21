package com.interview.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.dto.interview.InterviewReport;
import com.interview.dto.interview.QuestionEvaluationDto;
import com.interview.entity.InterviewAnswer;
import com.interview.entity.InterviewSession;
import com.interview.repository.InterviewAnswerRepository;
import com.interview.repository.InterviewSessionRepository;
import com.interview.service.GroqService;
import com.interview.service.InterviewReportService;


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
    @Transactional
    public InterviewReport generateReport(Long interviewId) {


        System.out.println(
                "========== GENERATE REPORT START =========="
        );

        System.out.println(
                "Interview ID = " + interviewId
        );


        // ==========================================
        // 1. GET SESSION
        // ==========================================

        InterviewSession session =
                sessionRepository.findById(interviewId)
                .orElseThrow(() ->
                        new RuntimeException("Interview not found")
                );


        System.out.println(
                "Existing report json = "
                + session.getReportJson()
        );


        // ==========================================
        // 2. RETURN EXISTING REPORT
        // ==========================================

        if(session.getReportJson() != null &&
           !session.getReportJson().isBlank()) {


            try {

                System.out.println(
                        "Returning saved report"
                );


                return objectMapper.readValue(
                        session.getReportJson(),
                        InterviewReport.class
                );


            } catch(Exception e){

                throw new RuntimeException(
                        "Invalid saved report"
                );
            }
        }



        // ==========================================
        // 3. FETCH ANSWERS
        // ==========================================


        List<InterviewAnswer> answers =
                answerRepository
                .findByInterviewSessionIdOrderByQuestionNumber(
                        interviewId
                );


        System.out.println(
                "Answer count = "
                + answers.size()
        );


        System.out.println(
                "Expected count = "
                + session.getQuestionCount()
        );



        // ==========================================
        // 4. COMPLETION CHECK
        // ==========================================


        if(answers.size() < session.getQuestionCount()) {


            throw new RuntimeException(
                    "Interview is incomplete."
            );

        }



        for(InterviewAnswer answer : answers){


            if(answer.getAnswer()==null ||
               answer.getAnswer().isBlank()){


                throw new RuntimeException(
                        "Interview is incomplete."
                );

            }

        }



        try {


            // ==========================================
            // 5. BUILD PROMPT
            // ==========================================


            StringBuilder prompt =
                    new StringBuilder();


            prompt.append("""
                    
                    You are a Senior Technical Interviewer.

                    Generate interview performance report.

                    Return ONLY JSON.

                    {
                    "overallScore":0,
                    "percentage":0,
                    "technicalKnowledge":0,
                    "communication":0,
                    "problemSolving":0,
                    "strengths":[],
                    "weaknesses":[],
                    "recommendation":""
                    }

                    """);



            for(InterviewAnswer answer: answers){


                prompt.append("\nQuestion:\n")
                .append(answer.getQuestion());


                prompt.append("\nAnswer:\n")
                .append(answer.getAnswer());


                prompt.append("\nFeedback:\n")
                .append(answer.getFeedback());


                prompt.append("\nScore:\n")
                .append(answer.getScore());

            }



            // ==========================================
            // 6. CALL GROQ
            // ==========================================


            String response =
                    groqService.generateResponse(
                            prompt.toString()
                    );



            response =
                    response
                    .replaceAll("```json","")
                    .replaceAll("```","")
                    .trim();



            System.out.println(
                    "========== AI RESPONSE =========="
            );

            System.out.println(response);



            InterviewReport report =
                    objectMapper.readValue(
                            response,
                            InterviewReport.class
                    );



            // ==========================================
            // 7. ADD DETAILS
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
            // 8. QUESTION REPORT
            // ==========================================


            List<QuestionEvaluationDto> questionReports =
                    new java.util.ArrayList<>();


            for(InterviewAnswer answer: answers){


                QuestionEvaluationDto dto =
                        new QuestionEvaluationDto();


                dto.setQuestionNumber(
                        answer.getQuestionNumber()
                );


                dto.setQuestion(
                        answer.getQuestion()
                );


                dto.setAnswer(
                        answer.getAnswer()
                );


                dto.setScore(
                        answer.getScore()
                );


                dto.setFeedback(
                        answer.getFeedback()
                );


                dto.setIdealAnswer(
                        answer.getIdealAnswer()
                );


                questionReports.add(dto);

            }


            report.setQuestions(questionReports);



            // ==========================================
            // 9. SAVE JSON
            // ==========================================


            String savedJson =
                    objectMapper.writeValueAsString(report);



            System.out.println(
                    "Saving JSON length = "
                    + savedJson.length()
            );


            System.out.println(
                    "Saving Interview ID = "
                    + session.getId()
            );



            // VERY IMPORTANT
            session.setReportJson(savedJson);



            session.setOverallScore(
                    report.getOverallScore()
            );


            session.setPercentage(
                    report.getPercentage()
            );



            sessionRepository.saveAndFlush(session);



            System.out.println(
                    "========== REPORT SAVED =========="
            );



            return report;



        }
        catch(Exception e){


            e.printStackTrace();


            throw new RuntimeException(
                    "Failed to generate interview report",
                    e
            );

        }

    }

}