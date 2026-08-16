package com.interview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.interview.dto.interview.InterviewReport;
import com.interview.dto.interview.StartInterviewRequest;
import com.interview.dto.interview.StartInterviewResponse;
import com.interview.dto.interview.SubmitAnswerRequest;
import com.interview.dto.interview.SubmitAnswerResponse;
import com.interview.service.AiInterviewService;
import com.interview.service.InterviewReportService;

import java.util.List;
import com.interview.dto.interview.InterviewHistoryDto;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin(origins = {
	    "http://127.0.0.1:5500",
	    "http://localhost:5500"
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
            @PathVariable Long userId){

        return aiInterviewService.getInterviewHistory(userId);

    }

}