package com.interview.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.interview.dto.compiler.CodeExecutionRequest;
import com.interview.dto.compiler.CodeExecutionResponse;
import com.interview.service.CompilerService;

@Service
public class CompilerServiceImpl implements CompilerService {

    private static final int TIMEOUT_SECONDS = 10;

    @Override
    public CodeExecutionResponse executeCode(CodeExecutionRequest request) {

        if (request == null) {
            return createErrorResponse("Request cannot be null.");
        }

        String language = request.getLanguage();
        String code = request.getCode();
        String input = request.getInput();

        if (language == null || language.isBlank()) {
            return createErrorResponse(
                    "Programming language is required."
            );
        }

        if (code == null || code.isBlank()) {
            return createErrorResponse(
                    "Code cannot be empty."
            );
        }

        /*
         * Convert values such as:
         *
         * java language -> java
         * c language -> c
         * c++ language -> cpp
         * javascript language -> javascript
         */
        language = normalizeLanguage(language);

        try {

            switch (language) {

                case "java":
                    return executeJava(code, input);

                case "python":
                    return executePython(code, input);

                case "javascript":
                    return executeJavaScript(code, input);

                case "c":
                    return executeC(code, input);

                case "cpp":
                    return executeCpp(code, input);

                case "csharp":
                    return executeCSharp(code, input);

                default:
                    return createErrorResponse(
                            "Unsupported programming language: "
                                    + language
                    );
            }

        } catch (Exception e) {

            e.printStackTrace();

            return createErrorResponse(
                    "Compilation/Execution error: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // LANGUAGE NORMALIZATION
    // =========================================================

    private String normalizeLanguage(String language) {

        if (language == null) {
            return "";
        }

        String normalized = language
                .trim()
                .toLowerCase();

        // Remove extra spaces
        normalized = normalized.replaceAll("\\s+", " ");

        // ---------------------------------------------
        // JAVA
        // ---------------------------------------------

        if (normalized.equals("java")
                || normalized.equals("java language")
                || normalized.startsWith("java ")) {

            return "java";
        }

        // ---------------------------------------------
        // PYTHON
        // ---------------------------------------------

        if (normalized.equals("python")
                || normalized.equals("python language")
                || normalized.equals("py")) {

            return "python";
        }

        // ---------------------------------------------
        // JAVASCRIPT
        // ---------------------------------------------

        if (normalized.equals("javascript")
                || normalized.equals("javascript language")
                || normalized.equals("js")
                || normalized.equals("node")
                || normalized.equals("nodejs")) {

            return "javascript";
        }

        // ---------------------------------------------
        // C
        // ---------------------------------------------

        /*
         * Handles:
         *
         * c
         * c language
         * c languagd
         * C
         * C Language
         */

        if (normalized.equals("c")
                || normalized.equals("c language")
                || normalized.equals("c languagd")) {

            return "c";
        }

        // ---------------------------------------------
        // C++
        // ---------------------------------------------

        if (normalized.equals("c++")
                || normalized.equals("c++ language")
                || normalized.equals("cpp")
                || normalized.equals("cpp language")) {

            return "cpp";
        }

        // ---------------------------------------------
        // C#
        // ---------------------------------------------

        if (normalized.equals("c#")
                || normalized.equals("c# language")
                || normalized.equals("csharp")
                || normalized.equals("c sharp")) {

            return "csharp";
        }

        return normalized;
    }


    // =========================================================
    // JAVA
    // =========================================================

    private CodeExecutionResponse executeJava(
            String code,
            String input) throws Exception {

        Path tempDirectory =
                Files.createTempDirectory("interview-java-");

        Path javaFile =
                tempDirectory.resolve("Main.java");

        Files.writeString(
                javaFile,
                code,
                StandardCharsets.UTF_8
        );

        // -----------------------------------------------------
        // COMPILE
        // -----------------------------------------------------

        ProcessBuilder compileProcess =
                new ProcessBuilder(
                        "javac",
                        javaFile.toString()
                );

        compileProcess.redirectErrorStream(true);

        Process compiler =
                compileProcess.start();

        String compileOutput =
                readOutput(compiler);

        boolean compileFinished =
                compiler.waitFor(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

        if (!compileFinished) {

            compiler.destroyForcibly();

            return createErrorResponse(
                    "Compilation timed out."
            );
        }

        if (compiler.exitValue() != 0) {

            return createErrorResponse(
                    compileOutput
            );
        }

        // -----------------------------------------------------
        // RUN
        // -----------------------------------------------------

        ProcessBuilder runProcess =
                new ProcessBuilder(
                        "java",
                        "-cp",
                        tempDirectory.toString(),
                        "Main"
                );

        runProcess.redirectErrorStream(true);

        Process process =
                runProcess.start();

        writeInput(process, input);

        String output =
                readOutput(process);

        boolean finished =
                process.waitFor(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

        if (!finished) {

            process.destroyForcibly();

            return createErrorResponse(
                    "Program execution timed out."
            );
        }

        if (process.exitValue() != 0) {

            return createErrorResponse(
                    output
            );
        }

        return createSuccessResponse(output);
    }


    // =========================================================
    // PYTHON
    // =========================================================

    private CodeExecutionResponse executePython(
            String code,
            String input) throws Exception {

        Path tempDirectory =
                Files.createTempDirectory("interview-python-");

        Path pythonFile =
                tempDirectory.resolve("main.py");

        Files.writeString(
                pythonFile,
                code,
                StandardCharsets.UTF_8
        );

        ProcessBuilder processBuilder =
                new ProcessBuilder(
                        "python3",
                        pythonFile.toString()
                );

        processBuilder.redirectErrorStream(true);

        Process process =
                processBuilder.start();

        writeInput(process, input);

        String output =
                readOutput(process);

        boolean finished =
                process.waitFor(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

        if (!finished) {

            process.destroyForcibly();

            return createErrorResponse(
                    "Program execution timed out."
            );
        }

        if (process.exitValue() != 0) {

            return createErrorResponse(
                    output
            );
        }

        return createSuccessResponse(output);
    }


    // =========================================================
    // JAVASCRIPT
    // =========================================================

    private CodeExecutionResponse executeJavaScript(
            String code,
            String input) throws Exception {

        Path tempDirectory =
                Files.createTempDirectory("interview-js-");

        Path jsFile =
                tempDirectory.resolve("main.js");

        Files.writeString(
                jsFile,
                code,
                StandardCharsets.UTF_8
        );

        ProcessBuilder processBuilder =
                new ProcessBuilder(
                        "node",
                        jsFile.toString()
                );

        processBuilder.redirectErrorStream(true);

        Process process =
                processBuilder.start();

        writeInput(process, input);

        String output =
                readOutput(process);

        boolean finished =
                process.waitFor(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

        if (!finished) {

            process.destroyForcibly();

            return createErrorResponse(
                    "Program execution timed out."
            );
        }

        if (process.exitValue() != 0) {

            return createErrorResponse(
                    output
            );
        }

        return createSuccessResponse(output);
    }


    // =========================================================
    // C
    // =========================================================

    private CodeExecutionResponse executeC(
            String code,
            String input) throws Exception {

        Path tempDirectory =
                Files.createTempDirectory("interview-c-");

        Path sourceFile =
                tempDirectory.resolve("main.c");

        Path executable =
                tempDirectory.resolve("main");

        Files.writeString(
                sourceFile,
                code,
                StandardCharsets.UTF_8
        );

        // -----------------------------------------------------
        // COMPILE C
        // -----------------------------------------------------

        ProcessBuilder compileProcess =
                new ProcessBuilder(
                        "gcc",
                        sourceFile.toString(),
                        "-o",
                        executable.toString()
                );

        compileProcess.redirectErrorStream(true);

        Process compiler =
                compileProcess.start();

        String compileOutput =
                readOutput(compiler);

        boolean compileFinished =
                compiler.waitFor(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

        if (!compileFinished) {

            compiler.destroyForcibly();

            return createErrorResponse(
                    "C compilation timed out."
            );
        }

        if (compiler.exitValue() != 0) {

            return createErrorResponse(
                    compileOutput
            );
        }

        // -----------------------------------------------------
        // RUN C
        // -----------------------------------------------------

        ProcessBuilder runProcess =
                new ProcessBuilder(
                        executable.toString()
                );

        runProcess.redirectErrorStream(true);

        Process process =
                runProcess.start();

        writeInput(process, input);

        String output =
                readOutput(process);

        boolean finished =
                process.waitFor(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

        if (!finished) {

            process.destroyForcibly();

            return createErrorResponse(
                    "C program execution timed out."
            );
        }

        if (process.exitValue() != 0) {

            return createErrorResponse(
                    output
            );
        }

        return createSuccessResponse(output);
    }


    // =========================================================
    // C++
    // =========================================================

    private CodeExecutionResponse executeCpp(
            String code,
            String input) throws Exception {

        Path tempDirectory =
                Files.createTempDirectory("interview-cpp-");

        Path sourceFile =
                tempDirectory.resolve("main.cpp");

        Path executable =
                tempDirectory.resolve("main");

        Files.writeString(
                sourceFile,
                code,
                StandardCharsets.UTF_8
        );

        // -----------------------------------------------------
        // COMPILE C++
        // -----------------------------------------------------

        ProcessBuilder compileProcess =
                new ProcessBuilder(
                        "g++",
                        sourceFile.toString(),
                        "-o",
                        executable.toString()
                );

        compileProcess.redirectErrorStream(true);

        Process compiler =
                compileProcess.start();

        String compileOutput =
                readOutput(compiler);

        boolean compileFinished =
                compiler.waitFor(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

        if (!compileFinished) {

            compiler.destroyForcibly();

            return createErrorResponse(
                    "C++ compilation timed out."
            );
        }

        if (compiler.exitValue() != 0) {

            return createErrorResponse(
                    compileOutput
            );
        }

        // -----------------------------------------------------
        // RUN C++
        // -----------------------------------------------------

        ProcessBuilder runProcess =
                new ProcessBuilder(
                        executable.toString()
                );

        runProcess.redirectErrorStream(true);

        Process process =
                runProcess.start();

        writeInput(process, input);

        String output =
                readOutput(process);

        boolean finished =
                process.waitFor(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

        if (!finished) {

            process.destroyForcibly();

            return createErrorResponse(
                    "C++ program execution timed out."
            );
        }

        if (process.exitValue() != 0) {

            return createErrorResponse(
                    output
            );
        }

        return createSuccessResponse(output);
    }


    // =========================================================
    // C#
    // =========================================================

    private CodeExecutionResponse executeCSharp(
            String code,
            String input) throws Exception {

        /*
         * C# requires the .NET SDK.
         *
         * We use dotnet script execution through
         * a temporary console project.
         */

        Path tempDirectory =
                Files.createTempDirectory(
                        "interview-csharp-"
                );

        Path projectDirectory =
                tempDirectory.resolve("project");

        Files.createDirectories(projectDirectory);

        Path projectFile =
                projectDirectory.resolve(
                        "Interview.csproj"
                );

        Path sourceFile =
                projectDirectory.resolve(
                        "Program.cs"
                );

        String projectContent = """
                <Project Sdk="Microsoft.NET.Sdk">
                    <PropertyGroup>
                        <OutputType>Exe</OutputType>
                        <TargetFramework>net8.0</TargetFramework>
                        <ImplicitUsings>enable</ImplicitUsings>
                        <Nullable>enable</Nullable>
                    </PropertyGroup>
                </Project>
                """;

        Files.writeString(
                projectFile,
                projectContent,
                StandardCharsets.UTF_8
        );

        Files.writeString(
                sourceFile,
                code,
                StandardCharsets.UTF_8
        );

        // -----------------------------------------------------
        // BUILD
        // -----------------------------------------------------

        ProcessBuilder buildProcess =
                new ProcessBuilder(
                        "dotnet",
                        "build",
                        projectDirectory.toString(),
                        "--nologo"
                );

        buildProcess.redirectErrorStream(true);

        Process builder =
                buildProcess.start();

        String buildOutput =
                readOutput(builder);

        boolean buildFinished =
                builder.waitFor(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

        if (!buildFinished) {

            builder.destroyForcibly();

            return createErrorResponse(
                    "C# compilation timed out."
            );
        }

        if (builder.exitValue() != 0) {

            return createErrorResponse(
                    buildOutput
            );
        }

        // -----------------------------------------------------
        // RUN
        // -----------------------------------------------------

        ProcessBuilder runProcess =
                new ProcessBuilder(
                        "dotnet",
                        "run",
                        "--project",
                        projectDirectory.toString(),
                        "--no-build"
                );

        runProcess.redirectErrorStream(true);

        Process process =
                runProcess.start();

        writeInput(process, input);

        String output =
                readOutput(process);

        boolean finished =
                process.waitFor(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

        if (!finished) {

            process.destroyForcibly();

            return createErrorResponse(
                    "C# program execution timed out."
            );
        }

        if (process.exitValue() != 0) {

            return createErrorResponse(
                    output
            );
        }

        return createSuccessResponse(output);
    }


    // =========================================================
    // WRITE INPUT
    // =========================================================

    private void writeInput(
            Process process,
            String input) throws Exception {

        if (input != null && !input.isBlank()) {

            process.getOutputStream()
                    .write(
                            input.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            process.getOutputStream().flush();
        }

        process.getOutputStream().close();
    }


    // =========================================================
    // READ PROCESS OUTPUT
    // =========================================================

    private String readOutput(
            Process process) throws Exception {

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                process.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                );

        StringBuilder output =
                new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {

            output.append(line)
                    .append(System.lineSeparator());
        }

        return output.toString().trim();
    }


    // =========================================================
    // SUCCESS RESPONSE
    // =========================================================

    private CodeExecutionResponse createSuccessResponse(
            String output) {

        CodeExecutionResponse response =
                new CodeExecutionResponse();

        response.setSuccess(true);
        response.setOutput(output);
        response.setError(null);

        return response;
    }


    // =========================================================
    // ERROR RESPONSE
    // =========================================================

    private CodeExecutionResponse createErrorResponse(
            String error) {

        CodeExecutionResponse response =
                new CodeExecutionResponse();

        response.setSuccess(false);
        response.setOutput("");
        response.setError(error);

        return response;
    }
}