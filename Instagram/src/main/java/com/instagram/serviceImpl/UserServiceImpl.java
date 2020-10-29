package com.instagram.serviceImpl;

import com.instagram.models.User;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UserServiceImpl{

  User findUserByEmail(String userEmail);

  User findUserByUsername(String username);

  User findUserByToken(String userToken);

  User findUserByUserId(UUID userId);

  UUID convertToUUID(String userId);

  ResponseEntity<?> updatePrivacy(String username, String privacy);

  void updateUser(User user);
}
