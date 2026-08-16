package com.interview.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.interview.dto.groq.ChatRequest;
import com.interview.dto.groq.ChatResponse;
import com.interview.dto.groq.Message;
import com.interview.service.GroqService;

@Service
public class GroqServiceImpl implements GroqService {

    @Autowired
    private RestClient restClient;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.model}")
    private String model;

    @Override
    public String generateResponse(String prompt) {

        ChatRequest request = new ChatRequest(
                model,
                List.of(new Message("user", prompt))
        );

        ChatResponse response = restClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(request)
                .retrieve()
                .body(ChatResponse.class);

        return response.getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}