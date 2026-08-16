package com.interview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.interview.dto.interview.QuestionDto;
import com.interview.dto.interview.StartInterviewRequest;
import com.interview.service.QuestionGeneratorService;

@RestController
@RequestMapping("/api/questions")
public class QuestionTestController {

    @Autowired
    private QuestionGeneratorService questionGeneratorService;

    @PostMapping
    public List<QuestionDto> test(@RequestBody StartInterviewRequest request) {

    	return questionGeneratorService.generateQuestions(
    	        request.getTechnology(),
    	        request.getInterviewType(),
    	        request.getLevel(),
    	        request.getQuestionCount());

    }
}