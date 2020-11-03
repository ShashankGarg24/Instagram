package com.instagram.services;

import com.instagram.models.Media;
import com.instagram.models.User;
import com.instagram.repository.MediaRepo;
import com.instagram.repository.UserRepository;
import com.instagram.serviceImpl.UserServiceImpl;
import java.math.BigInteger;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService implements UserServiceImpl {

  @Autowired
  UserRepository userRepository;

  @Autowired
  MediaRepo mediaRepo;

  @Autowired
  FileUploadService fileUploadService;

  @Autowired
  FileDeletingService fileDeletingService;

  public User findUserByEmail(String userEmail){
    return userRepository.findByUserEmail(userEmail);
  }

  public User findUserByToken(String userToken){
    return userRepository.findByVerificationToken(userToken);
  }

  public User findUserByUsername(String username){
    return userRepository.findByUsername(username);
  }

  public User findUserByUserId(UUID userId){

    return userRepository.findByUserId(userId);
  }

  public void updateUser(User user){
    userRepository.save(user);
  }

  public ResponseEntity<?> updatePrivacy(String username, String privacy){
    User user = findUserByUsername(username);
    if(!user.getUserPrivacy().equals(privacy)){
      userRepository.updatePrivacy(privacy, username);
      return new ResponseEntity<>(username + " account set to " + privacy, HttpStatus.ACCEPTED);
    }
    else{
      return new ResponseEntity<>(username + " account is already set to " + privacy, HttpStatus.OK);
    }
  }

  public ResponseEntity<?> deleteProfilePic(UUID userId) {
    User user = userRepository.findByUserId(userId);
    if (user.getProfilePic() == null) {
      return new ResponseEntity<>("Profile pic not set", HttpStatus.EXPECTATION_FAILED);
    }

    Media pic = user.getProfilePic();
    fileDeletingService.deleteFile(pic.getMediaId().toString(), "instaPFP");
    user.setProfilePic(null);
    mediaRepo.delete(pic);
    userRepository.save(user);
    return new ResponseEntity<>("Profile pic removed!", HttpStatus.OK);
  }

  public ResponseEntity<?> updateProfilePic(MultipartFile image, UUID userId) throws Exception {
    User user = userRepository.findByUserId(userId);
    if(user.getProfilePic() != null){
        deleteProfilePic(userId);
    }
    Media profilePic = new Media();
    fileUploadService.fileUpload(image, profilePic.getMediaId().toString(), "instaPFP");
    user.setProfilePic(profilePic);
    profilePic.setUser(user);
    userRepository.save(user);

    return new ResponseEntity<>("Profile pic updated!", HttpStatus.OK);
  }

  public UUID convertToUUID(String userId){
   return new UUID(
    new BigInteger(userId.substring(0, 16), 16).longValue(),
    new BigInteger(userId.substring(16), 16).longValue());
  }

}
