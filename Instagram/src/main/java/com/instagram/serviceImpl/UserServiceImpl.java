package com.instagram.serviceImpl;

import com.instagram.models.User;
import java.util.UUID;

public interface UserServiceImpl{

  User findUserByEmail(String userEmail);

  User findUserByToken(String userToken);

  User findUserByUserId(UUID userId);

  UUID convertToUUID(String userId);
}
