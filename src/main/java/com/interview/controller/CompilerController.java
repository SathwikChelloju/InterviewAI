package com.interview.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interview.dto.compiler.CodeExecutionRequest;
import com.interview.dto.compiler.CodeExecutionResponse;
import com.interview.service.CompilerService;

@RestController
@RequestMapping("/api/compiler")
@CrossOrigin(origins = {
        "http://127.0.0.1:5500",
        "http://localhost:5500",
        "https://YOUR-FRONTEND.onrender.com"
})
public class CompilerController {

    private final CompilerService compilerService;

    public CompilerController(
            CompilerService compilerService) {

        this.compilerService = compilerService;
    }

    @PostMapping("/run")
    public CodeExecutionResponse runCode(
            @RequestBody CodeExecutionRequest request) {

        return compilerService.executeCode(request);
    }
}