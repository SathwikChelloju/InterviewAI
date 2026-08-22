package com.interview.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interview.dto.LoginRequestDTO;
import com.interview.dto.RegisterRequestDTO;
import com.interview.dto.UserResponseDTO;
import com.interview.entity.User;
import com.interview.service.UserService;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {
        "http://127.0.0.1:5500",
        "http://localhost:5500",
        "https://interviewai-frontend-zh92.onrender.com"
})
public class UserController {


    @Autowired
    private UserService userService;



    // ==============================
    // REGISTER USER
    // ==============================
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody RegisterRequestDTO request) {

        try {

            User user = new User();

            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());


            User savedUser =
                    userService.registerUser(user);



            UserResponseDTO response =
                    new UserResponseDTO(
                            savedUser.getId(),
                            savedUser.getName(),
                            savedUser.getEmail(),
                            savedUser.getRole()
                    );


            return new ResponseEntity<>(
                    response,
                    HttpStatus.CREATED
            );


        } catch (IllegalArgumentException e) {


            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());

        }
    }





    // ==============================
    // LOGIN USER
    // ==============================
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody LoginRequestDTO request) {


        try {


            User user =
                    userService.loginUser(
                            request.getEmail(),
                            request.getPassword()
                    );


            if(user == null) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(
                          Map.of(
                          "message",
                          "Invalid email or password"
                          )
                        );
            }



            UserResponseDTO response =
                    new UserResponseDTO(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getRole()
                    );


            return ResponseEntity.ok(response);



        } catch(IllegalArgumentException e) {


            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                        Map.of(
                        "message",
                        e.getMessage()
                        )
                    );

        }

    }





    // ==============================
    // VERIFY EMAIL OTP
    // ==============================
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(
            @RequestBody Map<String,String> request) {


        try {


            String email =
                    request.get("email");


            String code =
                    request.get("code");



            boolean verified =
                    userService.verifyUser(
                            email,
                            code
                    );



            if(verified) {


                return ResponseEntity.ok(
                        Map.of(
                        "message",
                        "Email verified successfully"
                        )
                );

            }



            return ResponseEntity
                    .badRequest()
                    .body(
                        Map.of(
                        "message",
                        "Invalid verification code"
                        )
                    );



        } catch(Exception e) {


            return ResponseEntity
                    .badRequest()
                    .body(
                        Map.of(
                        "message",
                        e.getMessage()
                        )
                    );

        }

    }





    // ==============================
    // RESEND OTP
    // ==============================
    @PostMapping("/resend-code")
    public ResponseEntity<?> resendCode(
            @RequestBody Map<String,String> request
    ){

        String email = request.get("email");


        boolean sent =
                userService.resendVerificationCode(email);



        if(sent){

            return ResponseEntity.ok(
                    Map.of(
                        "message",
                        "New OTP sent successfully"
                    )
            );

        }



        return ResponseEntity
                .badRequest()
                .body(
                    Map.of(
                        "message",
                        "Email not found"
                    )
                );

    }

}