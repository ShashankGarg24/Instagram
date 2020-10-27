package com.instagram.controllers;

import com.instagram.models.User;
import com.instagram.repository.UserRepository;
import com.instagram.serviceImpl.RegistrationImpl;
import com.instagram.services.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Verification {

  @Autowired
  RegistrationImpl registration;

  @Autowired
  UserRepository userRepository;

  @GetMapping("/verify/{token}")
  public ResponseEntity<?> verify(@PathVariable String token){
    User user = userRepository.findByVerificationToken(token);
    if(user!=null){
      user.setVerified(true);
      userRepository.save(user);
      return new ResponseEntity<>("Successfully verified!", HttpStatus.OK);
    }
    else{
      return new ResponseEntity<>("Verification error", HttpStatus.NOT_FOUND);
    }
  }
}
