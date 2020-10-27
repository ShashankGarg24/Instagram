package com.instagram.services;

import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

  @Autowired
  JavaMailSender javaMailSender;

  @Async
  public void sendMail(String userEmail, String subject, String senderName, String mailContent) {

    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    try {
      helper.setFrom("${spring.mail.username}", senderName);
      helper.setTo(userEmail);
      helper.setSubject(subject);
      helper.setText(mailContent, true);

      javaMailSender.send(message);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
