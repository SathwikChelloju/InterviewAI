package com.interview.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interview.dto.interview.AnswerEvaluation;
import com.interview.dto.interview.InterviewHistoryDto;
import com.interview.dto.interview.QuestionDto;
import com.interview.dto.interview.StartInterviewRequest;
import com.interview.dto.interview.StartInterviewResponse;
import com.interview.dto.interview.SubmitAnswerRequest;
import com.interview.dto.interview.SubmitAnswerResponse;
import com.interview.entity.InterviewAnswer;
import com.interview.entity.InterviewSession;
import com.interview.entity.User;
import com.interview.repository.InterviewAnswerRepository;
import com.interview.repository.InterviewSessionRepository;
import com.interview.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.dto.interview.SampleTestCase;

@Service
public class AiInterviewService {

    @Autowired
    private QuestionGeneratorService questionGeneratorService;

    @Autowired
    private InterviewSessionRepository sessionRepository;

    @Autowired
    private InterviewAnswerRepository answerRepository;

    @Autowired
    private AnswerEvaluationService answerEvaluationService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ObjectMapper objectMapper;


    // ============================================================
    // START INTERVIEW
    // ============================================================

    public StartInterviewResponse startInterview(
            StartInterviewRequest request) {


        // ========================================================
        // 1. FIND USER
        // ========================================================

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );


        // ========================================================
        // 2. GENERATE QUESTIONS
        // ========================================================

        List<QuestionDto> questions =
                questionGeneratorService.generateQuestions(
                        request.getTechnology(),
                        request.getInterviewType(),
                        request.getLevel(),
                        request.getQuestionCount()
                );


        if (questions == null || questions.isEmpty()) {

            throw new RuntimeException(
                    "No interview questions were generated."
            );
        }


        // ========================================================
        // 3. CREATE SESSION
        // ========================================================

        InterviewSession session = new InterviewSession();

        session.setUser(user);

        session.setTechnology(
                request.getTechnology()
        );

        session.setInterviewType(
                request.getInterviewType()
        );

        session.setQuestionCount(
                request.getQuestionCount()
        );

        session.setLevel(
                request.getLevel()
        );

        session.setCurrentQuestion(1);

        session.setCompleted(false);

        session.setStartedAt(
                LocalDateTime.now()
        );


        session = sessionRepository.save(session);


        // ========================================================
        // 4. SAVE QUESTIONS
        // ========================================================

        for (QuestionDto q : questions) {

            InterviewAnswer answer = new InterviewAnswer();

            answer.setInterviewSession(session);

            answer.setQuestionNumber(q.getNumber());

            answer.setQuestion(q.getQuestion());

            try {

                if (q.getSampleTestCases() != null) {

                    answer.setSampleTestCases(
                            objectMapper.writeValueAsString(
                                    q.getSampleTestCases()
                            )
                    );
                }

            } catch (JsonProcessingException e) {

                throw new RuntimeException(
                        "Failed to store sample test cases",
                        e
                );
            }

            answerRepository.save(answer);
        }


        // ========================================================
        // 5. FIRST QUESTION
        // ========================================================

        QuestionDto firstQuestion =
                questions.get(0);


        StartInterviewResponse response =
                new StartInterviewResponse();


        response.setInterviewId(
                session.getId()
        );

        response.setQuestionNumber(
                firstQuestion.getNumber()
        );

        response.setQuestion(
                firstQuestion.getQuestion()
        );

        response.setSampleTestCases(
                firstQuestion.getSampleTestCases()
        );


        return response;
    }


    // ============================================================
    // SUBMIT ANSWER
    // ============================================================

    public SubmitAnswerResponse submitAnswer(
            SubmitAnswerRequest request) {


        InterviewAnswer interviewAnswer =
                answerRepository
                        .findByInterviewSessionIdAndQuestionNumber(
                                request.getInterviewId(),
                                request.getQuestionNumber()
                        );


        if (interviewAnswer == null) {

            throw new RuntimeException(
                    "Interview question not found."
            );
        }


        InterviewSession session =
                sessionRepository.findById(
                        request.getInterviewId()
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Interview session not found."
                        )
                );


        // ========================================================
        // AI EVALUATION
        // ========================================================

        AnswerEvaluation evaluation =
                answerEvaluationService.evaluate(
                        interviewAnswer.getQuestion(),
                        request.getAnswer(),
                        session.getInterviewType(),
                        session.getTechnology()
                );


        // ========================================================
        // SAVE ANSWER
        // ========================================================

        interviewAnswer.setAnswer(
                request.getAnswer()
        );

        interviewAnswer.setScore(
                evaluation.getScore()
        );

        interviewAnswer.setFeedback(
                evaluation.getFeedback()
        );

        interviewAnswer.setIdealAnswer(
                evaluation.getIdealAnswer()
        );


        answerRepository.save(
                interviewAnswer
        );


        // ========================================================
        // RESPONSE
        // ========================================================

        SubmitAnswerResponse response =
                new SubmitAnswerResponse();


        response.setScore(
                evaluation.getScore()
        );

        response.setFeedback(
                evaluation.getFeedback()
        );


        // ========================================================
        // THEORY RESPONSE
        // ========================================================

        if ("Theory".equalsIgnoreCase(
                session.getInterviewType())) {


            response.setIdealAnswer(
                    evaluation.getIdealAnswer()
            );
        }


        // ========================================================
        // CODING RESPONSE
        // ========================================================

        else if ("Coding".equalsIgnoreCase(
                session.getInterviewType())) {


            response.setCorrectCode(
                    evaluation.getCorrectCode()
            );

            response.setCodeExplanation(
                    evaluation.getCodeExplanation()
            );

            response.setTimeComplexity(
                    evaluation.getTimeComplexity()
            );

            response.setSpaceComplexity(
                    evaluation.getSpaceComplexity()
            );

            response.setTestCases(
                    evaluation.getTestCases()
            );
        }


        // ========================================================
        // INTERVIEW COMPLETED
        // ========================================================

        if (request.getQuestionNumber()
                == session.getQuestionCount()) {


            session.setCompleted(true);

            sessionRepository.save(session);


            response.setInterviewCompleted(true);


            return response;
        }


        // ========================================================
        // NEXT QUESTION
        // ========================================================

        int nextQuestion =
                request.getQuestionNumber() + 1;


        session.setCurrentQuestion(
                nextQuestion
        );


        sessionRepository.save(
                session
        );


        InterviewAnswer next =
                answerRepository
                        .findByInterviewSessionIdAndQuestionNumber(
                                request.getInterviewId(),
                                nextQuestion
                        );


        if (next == null) {

            throw new RuntimeException(
                    "Next interview question not found."
            );
        }


        response.setInterviewCompleted(false);

        response.setNextQuestionNumber(
                nextQuestion
        );

        response.setNextQuestion(
                next.getQuestion()
        );
        try {

            if (next.getSampleTestCases() != null) {

                List<SampleTestCase> nextTests =
                        objectMapper.readValue(
                                next.getSampleTestCases(),
                                objectMapper.getTypeFactory()
                                        .constructCollectionType(
                                                List.class,
                                                SampleTestCase.class
                                        )
                        );

                response.setNextSampleTestCases(nextTests);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to load next question sample test cases",
                    e
            );
        }

        return response;
    }


    // ============================================================
    // INTERVIEW HISTORY
    // ============================================================

    public List<InterviewHistoryDto> getInterviewHistory(
            Long userId) {


        List<InterviewSession> sessions =
                sessionRepository
                        .findByUserIdOrderByStartedAtDesc(
                                userId
                        );


        List<InterviewHistoryDto> history =
                new ArrayList<>();


        for (InterviewSession session : sessions) {


            InterviewHistoryDto dto =
                    new InterviewHistoryDto();


            dto.setInterviewId(
                    session.getId()
            );

            dto.setTechnology(
                    session.getTechnology()
            );

            dto.setInterviewType(
                    session.getInterviewType()
            );

            dto.setLevel(
                    session.getLevel()
            );

            dto.setOverallScore(
                    session.getOverallScore()
            );

            dto.setPercentage(
                    session.getPercentage()
            );

            dto.setCompleted(
                    session.getCompleted()
            );

            dto.setStartedAt(
                    session.getStartedAt()
            );


            history.add(dto);
        }


        return history;
    }
}