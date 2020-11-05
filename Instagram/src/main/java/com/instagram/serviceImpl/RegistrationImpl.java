package com.instagram.serviceImpl;

import com.instagram.Exceptions.ConfirmPasswordDoNotMatch;
import com.instagram.Exceptions.EmptyField;
import com.instagram.Exceptions.PasswordException;
import com.instagram.Exceptions.UserEmailAlreadyExist;
import com.instagram.Exceptions.UsernameAlreadyExist;
import com.instagram.models.SignUp;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.instagram.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface RegistrationImpl {

  ResponseEntity<?> registerUser(SignUp user)
          throws UserEmailAlreadyExist, PasswordException, ConfirmPasswordDoNotMatch, EmptyField, ExecutionException;

  ResponseEntity<?> userDetails(String userEmail, String fullName, String username)
      throws EmptyField, UsernameAlreadyExist;

  ResponseEntity<?> validateOtp(String userEmail, int otpEntered) throws ExecutionException;

  ResponseEntity<?> resendOtp(String userEmail) throws ExecutionException;

  boolean usernameFound(String username);
  List<String> suggestions(String username);
}
