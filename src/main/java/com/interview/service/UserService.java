package com.interview.service;

import com.interview.entity.User;

public interface UserService {

    User registerUser(User user);

    User getUserByEmail(String email);

    User loginUser(String email, String password);
}