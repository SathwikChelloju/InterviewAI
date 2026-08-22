package com.interview.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.interview.service.EmailService;


@RestController
@RequestMapping("/test")
public class TestEmailController {


    @Autowired
    private EmailService emailService;



    @GetMapping("/email")
    public String send(){

    	emailService.sendVerificationCode(
    		    "bunnysathwik17@gmail.com",
    		    "123456"
    		);


        return "Email sent";
    }

}