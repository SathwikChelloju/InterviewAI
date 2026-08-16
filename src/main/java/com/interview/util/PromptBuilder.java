package com.interview.util;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String buildFirstQuestionPrompt(String language, String level) {

        return """
            You are an experienced technical interviewer.

            Conduct a %s level interview on %s.

            Rules:
            1. Ask exactly ONE interview question.
            2. The question must be DIFFERENT every time.
            3. Do not repeat common questions.
            4. Cover different topics each request.
            5. Do not provide the answer.
            6. Return only the question.
            """
            .formatted(level, language);
    }

}