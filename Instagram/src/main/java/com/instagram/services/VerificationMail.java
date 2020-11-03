package com.instagram.services;

import com.instagram.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationMail {


  @Autowired
  MailService mailService;

  @Autowired
  OtpService otpService;

  public void sendVerificationEmail(User user){

    String subject = "OTP Verification";
    String senderName = "Instagram";
    String userEmail = user.getUserEmail();
    String mailContent = "<p>Dear User,  </p>";

    String site = "http://localhost:8080";

    mailContent += "<p>Your OTP is: " + otpService.generateOtp(user.getUserId().toString()) + "</p>";
    mailService.sendMail(userEmail ,subject,senderName,mailContent);
  }
}
