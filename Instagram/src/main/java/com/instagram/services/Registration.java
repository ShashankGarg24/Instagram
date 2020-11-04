package com.instagram.services;

import com.instagram.Exceptions.ConfirmPasswordDoNotMatch;
import com.instagram.Exceptions.EmptyField;
import com.instagram.Exceptions.PasswordException;
import com.instagram.Exceptions.UserEmailAlreadyExist;
import com.instagram.Exceptions.UsernameAlreadyExist;
import com.instagram.models.Media;
import com.instagram.models.SignUp;
import com.instagram.models.User;
import com.instagram.repository.UserRepository;
import com.instagram.serviceImpl.RegistrationImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.instagram.serviceImpl.UserServiceImpl;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Registration implements RegistrationImpl {

  @Autowired
  UserRepository userRepository;

  @Autowired
  VerificationMail verificationMail;

  @Autowired
  UserServiceImpl userService;

  @Autowired
  FileUploadService fileUploadService;

  @Autowired
  OtpService otpService;

  public ResponseEntity<?> registerUser(SignUp user)
          throws UserEmailAlreadyExist, PasswordException, EmptyField, ConfirmPasswordDoNotMatch, ExecutionException {


    if(user.getUserEmail() == null){
      throw new EmptyField();
    }

    User user_sub = userRepository.findByUserEmail(user.getUserEmail());
    if(userEmailFound(user.getUserEmail()) && !user_sub.isVerified()){
        verificationMail.sendVerificationEmail(user_sub);
        userService.updateUser(user_sub);
        return new ResponseEntity<String>("email already exist but not verified. Please check EMAIL to verify.",
                HttpStatus.NOT_ACCEPTABLE);

    }

    if(userEmailFound(user.getUserEmail()) && user_sub.isVerified()){
      throw new UserEmailAlreadyExist(user.getUserEmail());
    }

    emailValidator(user.getUserEmail());


    if(user.getPassword() == null){
      throw new EmptyField();
    }

    if(user.getPassword().length() < 6) {
      throw new PasswordException();
    }

    if(user.getConfirmPassword() == null){
      throw new EmptyField();
    }

    if(!user.getPassword().equals(user.getConfirmPassword())){
      throw new ConfirmPasswordDoNotMatch();
    }


    if(user.getRole() == null){
      user.setRole("USER");
    }

    try{

      User newUser = new User();

      newUser.setUserId(UUID.randomUUID());
      newUser.setUserPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
      newUser.setRole(user.getRole());
      newUser.setUserPrivacy("PUBLIC");
      newUser.setEnabled(true);
      newUser.setVerified(false);
      newUser.setUserEmail(user.getUserEmail());
      verificationMail.sendVerificationEmail(newUser);
      userRepository.save(newUser);

      return new ResponseEntity<>(newUser,HttpStatus.OK);

    }
    catch (PatternSyntaxException p){
      return new ResponseEntity<>("invalid email", HttpStatus.BAD_REQUEST);
    }
    catch (Exception e){
      return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
  }

  public ResponseEntity<?> validateOtp(UUID userId, int otpEntered) throws ExecutionException {
      int otp = otpService.getOtp(userId.toString());
      if(otpEntered == otp){
        userRepository.verifyUser(userId);
        otpService.clearOtp(userId.toString());
        return new ResponseEntity<>("Entered OTP is correct!", HttpStatus.ACCEPTED);
      }
      else{
        return new ResponseEntity<>("Entered OTP is wrong!", HttpStatus.BAD_REQUEST);
      }
  }

  public ResponseEntity<?> resendOtp(UUID userId) throws ExecutionException {

    otpService.clearOtp(userId.toString());
    verificationMail.sendVerificationEmail(userService.findUserByUserId(userId));
    return new ResponseEntity<>("New OTP sent!", HttpStatus.OK);
  }


  public ResponseEntity<?> userDetails(MultipartFile image, UUID userId, String fullName, String username, String userBio)
      throws EmptyField, UsernameAlreadyExist {


    if(fullName == null){
      throw new EmptyField();
    }

    if(username == null){
      throw new EmptyField();
    }

    if(usernameFound(username)){
      throw new UsernameAlreadyExist(username);
    }

    usernameValidator(username);

    try{

      User user = userRepository.findByUserId(userId);

      if(image != null){
        Media media = new Media();
        fileUploadService.fileUpload(image, media.getMediaId().toString(), "instaPFP");
        user.setProfilePic(media);
        userRepository.save(user);
      }
      userRepository.updateInitialDetails(fullName, username, userBio, userId);

      return new ResponseEntity<>(user, HttpStatus.OK);
    }
    catch (PatternSyntaxException p){
      return new ResponseEntity<>("invalid username", HttpStatus.BAD_REQUEST);
    }
    catch(Exception e){
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

  }


  public List<String> suggestions(String username){
    List<String> suggestions = new ArrayList<>();
    int count = 5;
    String suggestion;
    while(count > 0){
      suggestion = username + generateRandomNumber(100, 1000);
      if(usernameFound(suggestion)){
        continue;
      }
      else{
        suggestions.add(suggestion);
        count--;
      }
    }
    return suggestions;
  }

  public int generateRandomNumber(int lowerLimit, int upperLimit){
    return (int)((Math.random() * (upperLimit - lowerLimit)) + lowerLimit);
  }
  public boolean usernameFound(String username){
    return userRepository.findByUsername(username) != null;
  }

  public boolean userEmailFound(String userEmail){
    return userRepository.findByUserEmail(userEmail) != null;
  }

  public void emailValidator(String userEmail) {
    String email_regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    Pattern pattern = Pattern.compile(email_regex);
    Matcher matcher = pattern.matcher(userEmail);

    if (!matcher.matches()) {
      throw new PatternSyntaxException("not a valid address.", email_regex, 0);
    }
  }

    public void usernameValidator(String username){
      String username_regex = "^[a-z0-9._]{5,}$";
      Pattern pattern1 = Pattern.compile(username_regex);
      Matcher matcher1 = pattern1.matcher(username);

      if(!matcher1.matches()){
        throw new PatternSyntaxException("not a valid username.",username_regex,0);
      }

    }
}

