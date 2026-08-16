package com.interview.dto.interview;

import java.util.List;

public class StartInterviewResponse {

    private Long interviewId;

    private int questionNumber;

    private String question;

    private List<SampleTestCase> sampleTestCases;


    public StartInterviewResponse() {
    }


    public Long getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(Long interviewId) {
        this.interviewId = interviewId;
    }


    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public List<SampleTestCase> getSampleTestCases() {
        return sampleTestCases;
    }

    public void setSampleTestCases(
            List<SampleTestCase> sampleTestCases) {

        this.sampleTestCases = sampleTestCases;
    }
}