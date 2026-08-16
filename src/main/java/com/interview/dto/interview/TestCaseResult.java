package com.interview.dto.interview;

public class TestCaseResult {

    private String input;

    private String expectedOutput;

    private Boolean candidatePass;


    public TestCaseResult() {
    }


    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }


    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }


    public Boolean getCandidatePass() {
        return candidatePass;
    }

    public void setCandidatePass(Boolean candidatePass) {
        this.candidatePass = candidatePass;
    }
}