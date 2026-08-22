package com.interview.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.interview.entity.User;
import com.interview.repository.UserRepository;
import com.interview.service.EmailService;
import com.interview.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    
    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Override
    public User registerUser(User user) {

        // Check whether email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }


        // Set default role
        user.setRole("USER");


        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );


        // Generate 6 digit verification OTP
        String otp = String.valueOf(
                (int)(Math.random() * 900000) + 100000
        );


        // Save verification details
     // Save verification details
        user.setVerificationCode(otp);

        user.setVerified(false);

        user.setVerificationExpiry(
                LocalDateTime.now().plusMinutes(5)
        );


        // Save user
        User savedUser = userRepository.save(user);


        // Send OTP to registered email
        emailService.sendVerificationCode(
                savedUser.getEmail(),
                otp
        );


        return savedUser;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email).orElse(null);


        if (user == null) {
            return null;
        }


        // Check whether email is verified
        if (!user.isVerified()) {

            throw new IllegalArgumentException(
                    "Please verify your email before login"
            );
        }


        // Check password
        if (passwordEncoder.matches(
                password,
                user.getPassword()
        )) {

            return user;
        }


        return null;
    }
    @Override
    public boolean verifyUser(String email, String code) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        if(user.getVerificationCode().equals(code)) {


            // Check OTP expiry

            if(user.getVerificationExpiry()
                    .isBefore(LocalDateTime.now())) {


                return false;

            }



            user.setVerified(true);

            user.setVerificationCode(null);

            user.setVerificationExpiry(null);



            userRepository.save(user);


            return true;

        }

        return false;
    }
    @Override
    public boolean resendVerificationCode(String email) {


        User user = userRepository.findByEmail(email)
                .orElse(null);



        if(user == null){

            return false;

        }



        // Generate new OTP

        String otp = String.valueOf(
                (int)(Math.random() * 900000) + 100000
        );



        // Update OTP

        user.setVerificationCode(otp);

        user.setVerified(false);

        user.setVerificationExpiry(
                LocalDateTime.now().plusMinutes(5)
        );

        user.setVerificationExpiry(
                LocalDateTime.now().plusMinutes(5)
        );



        userRepository.save(user);



        // Send email

        emailService.sendVerificationCode(
                email,
                otp
        );



        return true;

    }
}