package com.instagram.serviceImpl;

import com.instagram.Exceptions.ConfirmPasswordDoNotMatch;
import com.instagram.Exceptions.EmptyField;
import com.instagram.Exceptions.PasswordException;
import com.instagram.Exceptions.UserEmailAlreadyExist;
import com.instagram.Exceptions.UsernameAlreadyExist;
import com.instagram.models.SignUp;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface RegistrationImpl {

  ResponseEntity<?> registerUser(SignUp user)
      throws UserEmailAlreadyExist, PasswordException, ConfirmPasswordDoNotMatch, EmptyField;

  ResponseEntity<?> userDetails(MultipartFile image, UUID userId,  String fullName, String username, String userBio)
      throws EmptyField, UsernameAlreadyExist;
}
