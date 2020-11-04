package com.instagram.services;

import com.instagram.models.Media;
import com.instagram.models.ResetDetails;
import com.instagram.models.User;
import com.instagram.repository.MediaRepo;
import com.instagram.repository.UserRepository;
import com.instagram.serviceImpl.UserServiceImpl;
import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

  @Autowired
  MailService mailService;

  @Autowired
  OtpService otpService;

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

  public ResponseEntity<?> resetPassword(ResetDetails userDetails){

    try {
      User user;
      user = userRepository.findByUsername(userDetails.getUsername());

      if (user == null) {
        return new ResponseEntity<String>("Username doesn't exist", HttpStatus.NOT_FOUND);
      }

      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      boolean check = encoder.matches(userDetails.getOldPassword(), user.getUserPassword());

      if (!check) {
        return new ResponseEntity<String>("Old Password is incorrect!", HttpStatus.BAD_REQUEST);
      }

      String newPassword = userDetails.getNewPassword();
      String confirmPassword = userDetails.getConfirmPassword();

      if (!newPassword.equals(confirmPassword)) {
        return new ResponseEntity<String>("Your new password and confirm password doesn't match!",
                HttpStatus.BAD_REQUEST);
      }

      userRepository.updatePassword(user.getUserId(), BCrypt.hashpw(newPassword, BCrypt.gensalt()));

      String mailContent = "<p>Password updated successfully</p>";
      mailService.sendMail(user.getUserEmail(), "Password updated", "Instagram", mailContent);
      return new ResponseEntity<String>("Password updated", HttpStatus.OK);

    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  public ResponseEntity<?> forgotPassword(String userEmail){
    try {
      if(userRepository.findByUserEmail(userEmail) == null){
        return new ResponseEntity<>("No account is registered by this email!", HttpStatus.NOT_FOUND);
      }
      otpService.clearOtp(userEmail);
      String mailContent = "Your otp is: " + otpService.generateOtp(userEmail);
      mailService.sendMail(userEmail, "OTP Verification", "Instagram", mailContent);
      return new ResponseEntity<>("otp sent to email.", HttpStatus.OK);
    } catch (ExecutionException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  public ResponseEntity<?> validateOtp(String userEmail, int otpEntered) throws ExecutionException {
    int otp = otpService.getOtp(userEmail);
    if(otpEntered == otp){
      otpService.clearOtp(userEmail);
      return new ResponseEntity<>("Entered OTP is correct!", HttpStatus.ACCEPTED);
    }
    else{
      return new ResponseEntity<>("Entered OTP is wrong!", HttpStatus.BAD_REQUEST);
    }
  }

  public ResponseEntity<?> resendOtp(String userEmail) throws ExecutionException {

    otpService.clearOtp(userEmail);
    String mailContent = "Your new otp is: " + otpService.generateOtp(userEmail);
    mailService.sendMail(userEmail, "OTP Verification", "Instagram", mailContent);
    return new ResponseEntity<>("new otp sent to email.", HttpStatus.OK);
  }



  public UUID convertToUUID(String userId){
   return new UUID(
    new BigInteger(userId.substring(0, 16), 16).longValue(),
    new BigInteger(userId.substring(16), 16).longValue());
  }

}
