package com.interview.service;

import com.interview.dto.compiler.CodeExecutionRequest;
import com.interview.dto.compiler.CodeExecutionResponse;

public interface CompilerService {

    CodeExecutionResponse executeCode(CodeExecutionRequest request);

}