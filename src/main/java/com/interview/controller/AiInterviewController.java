package com.interview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interview.dto.interview.InterviewHistoryDto;
import com.interview.dto.interview.InterviewReport;
import com.interview.dto.interview.StartInterviewRequest;
import com.interview.dto.interview.StartInterviewResponse;
import com.interview.dto.interview.SubmitAnswerRequest;
import com.interview.dto.interview.SubmitAnswerResponse;
import com.interview.service.AiInterviewService;
import com.interview.service.InterviewReportService;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin(origins = {
        "http://127.0.0.1:5500",
        "http://localhost:5500",
        "https://interviewai-frontend-zh92.onrender.com"
})
public class AiInterviewController {

    @Autowired
    private AiInterviewService aiInterviewService;

    @Autowired
    private InterviewReportService interviewReportService;

    @PostMapping("/start")
    public StartInterviewResponse startInterview(
            @RequestBody StartInterviewRequest request) {

        return aiInterviewService.startInterview(request);
    }

    @PostMapping("/answer")
    public SubmitAnswerResponse submitAnswer(
            @RequestBody SubmitAnswerRequest request) {

        return aiInterviewService.submitAnswer(request);
    }

    @GetMapping("/report/{interviewId}")
    public InterviewReport generateReport(
            @PathVariable Long interviewId) {

        return interviewReportService.generateReport(interviewId);
    }

    @GetMapping("/history/{userId}")
    public List<InterviewHistoryDto> getInterviewHistory(
            @PathVariable Long userId) {

        return aiInterviewService.getInterviewHistory(userId);
    }
}