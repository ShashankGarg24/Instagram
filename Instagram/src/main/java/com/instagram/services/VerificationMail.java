package com.instagram.services;

import com.instagram.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class VerificationMail {


  @Autowired
  MailService mailService;

  @Autowired
  OtpService otpService;

  public void sendVerificationEmail(User user) throws ExecutionException {

    String subject = "OTP Verification";
    String senderName = "Instagram";
    String userEmail = user.getUserEmail();
    String mailContent = "<p>Dear User,  </p>";
    otpService.clearOtp(user.getUserId().toString());
    mailContent += "<p>Your OTP is: " + otpService.generateOtp(user.getUserId().toString()) + "</p>";
    mailService.sendMail(userEmail ,subject,senderName,mailContent);
  }
}
