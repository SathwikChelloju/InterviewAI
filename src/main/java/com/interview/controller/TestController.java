package com.interview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interview.service.GroqService;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private GroqService groqService;

    @GetMapping
    public String test() {
        return groqService.generateResponse("Say hello in one sentence.");
    }
}