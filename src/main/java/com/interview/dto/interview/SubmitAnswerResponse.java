package com.interview.dto.interview;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmitAnswerResponse {

    // ==========================================
    // EVALUATION RESULT
    // ==========================================

    private Integer score;

    private String feedback;


    // ==========================================
    // THEORY
    // ==========================================

    private String idealAnswer;


    // ==========================================
    // CODING
    // ==========================================

    private String correctCode;

    private String codeExplanation;

    private String timeComplexity;

    private String spaceComplexity;

    private List<TestCaseResult> testCases;


    // ==========================================
    // INTERVIEW NAVIGATION
    // ==========================================

    private Boolean interviewCompleted;

    private Integer nextQuestionNumber;

    private String nextQuestion;


    // ==========================================
    // NEXT QUESTION SAMPLE TEST CASES
    // ==========================================

    private List<SampleTestCase> nextSampleTestCases;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public SubmitAnswerResponse() {
    }


    // ==========================================
    // SCORE
    // ==========================================

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }


    // ==========================================
    // FEEDBACK
    // ==========================================

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }


    // ==========================================
    // THEORY
    // ==========================================

    public String getIdealAnswer() {
        return idealAnswer;
    }

    public void setIdealAnswer(String idealAnswer) {
        this.idealAnswer = idealAnswer;
    }


    // ==========================================
    // CODING
    // ==========================================

    public String getCorrectCode() {
        return correctCode;
    }

    public void setCorrectCode(String correctCode) {
        this.correctCode = correctCode;
    }


    public String getCodeExplanation() {
        return codeExplanation;
    }

    public void setCodeExplanation(String codeExplanation) {
        this.codeExplanation = codeExplanation;
    }


    public String getTimeComplexity() {
        return timeComplexity;
    }

    public void setTimeComplexity(String timeComplexity) {
        this.timeComplexity = timeComplexity;
    }


    public String getSpaceComplexity() {
        return spaceComplexity;
    }

    public void setSpaceComplexity(String spaceComplexity) {
        this.spaceComplexity = spaceComplexity;
    }


    // ==========================================
    // EVALUATED TEST CASES
    // ==========================================

    public List<TestCaseResult> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCaseResult> testCases) {
        this.testCases = testCases;
    }


    // ==========================================
    // INTERVIEW STATUS
    // ==========================================

    public Boolean getInterviewCompleted() {
        return interviewCompleted;
    }

    public void setInterviewCompleted(Boolean interviewCompleted) {
        this.interviewCompleted = interviewCompleted;
    }


    // ==========================================
    // NEXT QUESTION
    // ==========================================

    public Integer getNextQuestionNumber() {
        return nextQuestionNumber;
    }

    public void setNextQuestionNumber(Integer nextQuestionNumber) {
        this.nextQuestionNumber = nextQuestionNumber;
    }


    public String getNextQuestion() {
        return nextQuestion;
    }

    public void setNextQuestion(String nextQuestion) {
        this.nextQuestion = nextQuestion;
    }


    // ==========================================
    // NEXT QUESTION SAMPLE TEST CASES
    // ==========================================

    public List<SampleTestCase> getNextSampleTestCases() {
        return nextSampleTestCases;
    }

    public void setNextSampleTestCases(
            List<SampleTestCase> nextSampleTestCases) {

        this.nextSampleTestCases = nextSampleTestCases;
    }
}