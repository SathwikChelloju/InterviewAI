package com.interview.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.interview.entity.User;
import com.interview.repository.UserRepository;
import com.interview.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Override
    public User registerUser(User user) {

        // Check whether email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setRole("USER");

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null &&
            passwordEncoder.matches(password, user.getPassword())) {

            return user;
        }

        return null;
    }
}