package com.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "interview_answers")
public class InterviewAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private InterviewSession interviewSession;

    private Integer questionNumber;

    @Column(length = 3000)
    private String question;

    @Column(length = 5000)
    private String answer;

    @Column(length = 5000)
    private String feedback;

    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String idealAnswer;

    // Sample test cases stored as JSON
    @Column(columnDefinition = "TEXT")
    private String sampleTestCases;


    public InterviewAnswer() {
    }


    public Long getId() {
        return id;
    }


    public InterviewSession getInterviewSession() {
        return interviewSession;
    }

    public void setInterviewSession(
            InterviewSession interviewSession) {

        this.interviewSession = interviewSession;
    }


    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(
            Integer questionNumber) {

        this.questionNumber = questionNumber;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }


    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }


    public String getIdealAnswer() {
        return idealAnswer;
    }

    public void setIdealAnswer(String idealAnswer) {
        this.idealAnswer = idealAnswer;
    }


    public String getSampleTestCases() {
        return sampleTestCases;
    }

    public void setSampleTestCases(
            String sampleTestCases) {

        this.sampleTestCases = sampleTestCases;
    }
}