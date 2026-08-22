package com.interview.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.interview.dto.interview.InterviewReport;
import com.interview.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String email, String code) {

        try {

            System.out.println("Preparing OTP email...");
            System.out.println("Receiver email : " + email);
            System.out.println("OTP Code : " + code);


            MimeMessage message = mailSender.createMimeMessage();


            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );


            helper.setFrom("interviewmate310@gmail.com");

            helper.setFrom("b65989001@smtp-brevo.com");
            helper.setTo(email);
            helper.setSubject("InterviewMate Email Verification");


            String htmlContent = """
                    YOUR EXISTING HTML CONTENT HERE
                    """
                    .replace(
                        "{{OTP_CODE}}",
                        code
                    );


            helper.setText(
                    htmlContent,
                    true
            );


            System.out.println("Sending OTP email...");

            mailSender.send(message);


            System.out.println("OTP email sent successfully");


        } catch(Exception e){

            System.err.println(
                    "Failed to send verification email"
            );

            e.printStackTrace();

        }
    }
    
    @Override
    public void sendInterviewReport(
            String email,
            InterviewReport report
    ) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();


            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );


            helper.setFrom("interviewmate310@gmail.com");

            helper.setTo(email);

            helper.setSubject(
                    "InterviewMate - Your Interview Performance Report"
            );


            String htmlContent = """
            <!DOCTYPE html>
            <html>

            <body style="
            font-family: Arial, sans-serif;
            background:#f4f5f7;
            padding:30px;
            ">

            <div style="
            max-width:600px;
            margin:auto;
            background:#111827;
            color:white;
            padding:30px;
            border-radius:15px;
            ">


            <h1 style="color:#6366f1;">
            InterviewMate
            </h1>


            <h2>
            Interview Performance Report
            </h2>


            <hr>


            <p>
            <b>Technology:</b>
            %s
            </p>


            <p>
            <b>Interview Type:</b>
            %s
            </p>


            <p>
            <b>Level:</b>
            %s
            </p>


            <h2>
            Overall Performance
            </h2>


            <p>
            Score:
            <b>%s</b>
            </p>


            <p>
            Percentage:
            <b>%s%%</b>
            </p>


            <h2>
            Strengths
            </h2>

            <ul>
            %s
            </ul>


            <h2>
            Areas To Improve
            </h2>

            <ul>
            %s
            </ul>


            <h2>
            Recommendation
            </h2>

            <p>
            %s
            </p>


            <br>

            <p>
            Thank you for using InterviewMate.
            </p>


            </div>

            </body>
            </html>

            """.formatted(

                    report.getTechnology(),

                    report.getInterviewType(),

                    report.getLevel(),

                    report.getOverallScore(),

                    report.getPercentage(),

                    convertListToHtml(
                            report.getStrengths()
                    ),

                    convertListToHtml(
                            report.getWeaknesses()
                    ),

                    report.getRecommendation()

            );


            helper.setText(
                    htmlContent,
                    true
            );


            mailSender.send(message);


        } catch(Exception e){

System.err.println(
"Failed to send interview report email"
);

e.printStackTrace();

}

    }
    
    private String convertListToHtml(
            java.util.List<String> list
    ){

        if(list == null || list.isEmpty()){
            return "<li>No details available</li>";
        }


        StringBuilder builder =
                new StringBuilder();


        for(String item : list){

            builder.append("<li>")
                   .append(item)
                   .append("</li>");

        }


        return builder.toString();
    }
}