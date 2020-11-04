package com.instagram.serviceImpl;

import com.instagram.models.ResetDetails;
import com.instagram.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface UserServiceImpl{

  User findUserByEmail(String userEmail);

  User findUserByUsername(String username);

  User findUserByToken(String userToken);

  User findUserByUserId(UUID userId);

  UUID convertToUUID(String userId);

  ResponseEntity<?> updatePrivacy(String username, String privacy);

  ResponseEntity<?> deleteProfilePic(UUID userId);

  ResponseEntity<?> updateProfilePic(MultipartFile image, UUID id) throws Exception;

  ResponseEntity<?> resetPassword(ResetDetails userDetails);

  ResponseEntity<?> forgotPassword(String userEmail);

  ResponseEntity<?> validateOtp(String userEmail, int otpEntered) throws ExecutionException;

  ResponseEntity<?> resendOtp(String userEmail) throws ExecutionException;

  void updateUser(User user);

}
