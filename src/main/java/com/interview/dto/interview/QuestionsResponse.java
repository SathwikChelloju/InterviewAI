package com.interview.dto.interview;

import java.util.List;

public class QuestionsResponse {

    private List<QuestionDto> questions;

    public QuestionsResponse() {
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }
}