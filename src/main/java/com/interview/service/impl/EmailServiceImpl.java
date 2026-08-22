package com.interview.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.interview.dto.interview.InterviewReport;
import com.interview.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import jakarta.mail.MessagingException;

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

            // Using the verified email address and professional display name
            helper.setFrom("interviewmate310@gmail.com", "InterviewMate");
            helper.setTo(email);
            helper.setSubject("InterviewMate Email Verification");

            // The full premium HTML template
            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                      <meta charset="utf-8">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <title>InterviewMate - OTP Verification</title>
                    </head>
                    <body style="margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f4f5f7;">
                      
                      <table width="100%" cellpadding="0" cellspacing="0" style="background-color: #f4f5f7; padding: 40px 20px;">
                        <tr>
                          <td align="center">
                            
                            <table width="100%" cellpadding="0" cellspacing="0" style="max-width: 500px; background-color: #111827; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.15);">
                              
                              <tr>
                                <td style="background-color: #4f46e5; padding: 35px 20px; text-align: center;">
                                  <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: 700; letter-spacing: -0.5px;">
                                    InterviewMate
                                  </h1>
                                  <p style="color: #c7d2fe; margin: 8px 0 0 0; font-size: 13px; font-weight: 500; letter-spacing: 1px; text-transform: uppercase;">
                                    Intelligent Interview Practice
                                  </p>
                                </td>
                              </tr>
                              
                              <tr>
                                <td style="padding: 45px 30px; text-align: center;">
                                  <h2 style="color: #ffffff; margin: 0 0 15px 0; font-size: 24px; font-weight: 600;">
                                    OTP Verification
                                  </h2>
                                  <p style="color: #9ca3af; margin: 0 0 35px 0; font-size: 15px; line-height: 1.6;">
                                    Thank you for joining <strong style="color: #ffffff;">InterviewMate</strong>. Please use the OTP below to complete your email verification.
                                  </p>

                                  <div style="background-color: rgba(99, 102, 241, 0.1); border: 2px solid #6366f1; border-radius: 12px; padding: 25px; margin: 0 auto; max-width: 320px;">
                                    <h1 style="color: #a5b4fc; margin: 0; font-size: 42px; letter-spacing: 14px; font-weight: 700; text-align: center;">
                                      {{OTP_CODE}}
                                    </h1>
                                  </div>

                                  <p style="color: #6b7280; margin: 25px 0 0 0; font-size: 13px;">
                                    Valid for 5 minutes
                                  </p>
                                </td>
                              </tr>
                              
                              <tr>
                                <td style="background-color: #050816; padding: 25px 20px; text-align: center; border-top: 1px solid #1f2937;">
                                  <p style="color: #6b7280; margin: 0; font-size: 12px;">
                                    &copy; 2026 <strong style="color: #9ca3af;">InterviewMate</strong> &bull; Master the technical interview
                                  </p>
                                </td>
                              </tr>

                            </table>

                          </td>
                        </tr>
                      </table>

                    </body>
                    </html>
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
            System.out.println("Connecting to Brevo...");

            mailSender.send(message);

            System.out.println("OTP EMAIL SENT SUCCESSFULLY");

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

            // Added professional display name here as well!
            helper.setFrom("interviewmate310@gmail.com", "InterviewMate");
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