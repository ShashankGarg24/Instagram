package com.instagram.services;

import com.instagram.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationMail {


  @Autowired
  MailService mailService;
  public void sendVerificationEmail(User user){

    String subject = "Please verify your registration.";
    String senderName = "Instagram";
    String userEmail = user.getUserEmail();
    String mailContent = "<p>Dear User,  </p>";

    String site = "http://localhost:8080";

    String verifyUrl = "/api/verify/" + user.getVerificationToken();
    mailContent += "<p>Please click the link to verify your email</p>";
    mailContent += "<a href='" + site + verifyUrl + "'>VERIFY</a><br>";

    mailService.sendMail(userEmail ,subject,senderName,mailContent);
  }
}
