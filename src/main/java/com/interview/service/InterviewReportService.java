package com.interview.service;

import com.interview.dto.interview.InterviewReport;

public interface InterviewReportService {

    InterviewReport generateReport(Long interviewId);

}