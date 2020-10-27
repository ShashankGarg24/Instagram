package com.instagram.services;

import com.instagram.models.User;
import com.instagram.repository.UserRepository;
import com.instagram.serviceImpl.UserServiceImpl;
import java.math.BigInteger;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceImpl {

  @Autowired
  UserRepository userRepository;

  public User findUserByEmail(String userEmail){
    return userRepository.findByUserEmail(userEmail);
  }

  public User findUserByToken(String userToken){
    return userRepository.findByVerificationToken(userToken);
  }

  public User findUserByUserId(UUID userId){

    return userRepository.findByUserId(userId);
  }

  public UUID convertToUUID(String userId){
   return new UUID(
    new BigInteger(userId.substring(0, 16), 16).longValue(),
    new BigInteger(userId.substring(16), 16).longValue());
  }

}
