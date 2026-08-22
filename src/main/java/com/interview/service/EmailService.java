package com.interview.service;

import com.interview.dto.interview.InterviewReport;

public interface EmailService {

    void sendVerificationCode(
            String email,
            String code
    );
    
    void sendInterviewReport(
            String email,
            InterviewReport report
    );

}