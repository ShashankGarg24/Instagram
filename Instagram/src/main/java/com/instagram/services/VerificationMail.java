package com.instagram.services;

import com.instagram.models.User;
import com.instagram.models.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class VerificationMail {


  @Autowired
  MailService mailService;

  @Autowired
  OtpService otpService;

  public void sendVerificationEmail(UserCredentials user) throws ExecutionException {

    String subject = "OTP Verification";
    String senderName = "Instagram";
    String userEmail = user.getUserEmail();
    String mailContent = "<p>Dear User,  </p>";
    otpService.clearOtp(userEmail);
    int otp = otpService.generateOtp(userEmail);
    System.out.println(otp);
    mailContent += "<p>Your OTP is: " + otp + "</p>";
    mailService.sendMail(userEmail ,subject,senderName,mailContent);
  }
}
