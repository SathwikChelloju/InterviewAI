package com.interview.dto.interview;

import java.util.List;

public class QuestionDto {

    private int number;

    private String question;

    private List<SampleTestCase> sampleTestCases;


    public QuestionDto() {
    }


    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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