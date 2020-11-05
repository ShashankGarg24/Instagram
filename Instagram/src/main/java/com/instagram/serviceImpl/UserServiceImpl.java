package com.instagram.serviceImpl;

import com.instagram.models.ResetDetails;
import com.instagram.models.User;
import com.instagram.models.UserCredentials;
import com.instagram.models.UserProfile;
import org.checkerframework.common.value.qual.StringVal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface UserServiceImpl{

  UserCredentials findUserByEmail(String userEmail);

  UserProfile findUserByUsername(String username);

  User findUserByToken(String userToken);

  User findUserByUserId(UUID userId);

  UUID convertToUUID(String userId);

  ResponseEntity<?> updatePrivacy(String username, String privacy);

  ResponseEntity<?> deleteProfilePic(UUID userId);

  ResponseEntity<?> updateProfilePic(MultipartFile image, UUID id) throws Exception;

  ResponseEntity<?> resetPassword(ResetDetails userDetails);

  ResponseEntity<?> setNewPassword(String userEmail, String password);

  ResponseEntity<?> forgotPassword(String userEmail);

  void updateUser(UserCredentials user);

}
