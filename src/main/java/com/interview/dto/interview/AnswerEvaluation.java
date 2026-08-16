package com.interview.dto.interview;

import java.util.List;

public class AnswerEvaluation {

    private Integer score;

    private String feedback;

    private String idealAnswer;
    private String correctCode;
    private String codeExplanation;
    private String timeComplexity;
    private String spaceComplexity;
    private List<TestCaseResult> testCases;

    public List<TestCaseResult> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCaseResult> testCases) {
		this.testCases = testCases;
	}

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

	public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getIdealAnswer() {
        return idealAnswer;
    }

    public void setIdealAnswer(String idealAnswer) {
        this.idealAnswer = idealAnswer;
    }
}