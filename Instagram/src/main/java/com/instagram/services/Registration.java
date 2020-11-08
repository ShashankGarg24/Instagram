package com.instagram.services;

import com.instagram.Exceptions.ConfirmPasswordDoNotMatch;
import com.instagram.Exceptions.EmptyField;
import com.instagram.Exceptions.PasswordException;
import com.instagram.Exceptions.UserEmailAlreadyExist;
import com.instagram.Exceptions.UsernameAlreadyExist;
import com.instagram.models.*;
import com.instagram.repository.ProfileRepository;
import com.instagram.repository.UserCredentialsRepo;
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
  UserCredentialsRepo userCredentialsRepo;

  @Autowired
  ProfileRepository profileRepository;

  @Autowired
  VerificationMail verificationMail;

  @Autowired
  UserServiceImpl userService;

  @Autowired
  FileUploadService fileUploadService;

  @Autowired
  OtpService otpService;

  public ResponseEntity<?> registerUser(SignUp user)
          throws UserEmailAlreadyExist, PasswordException, EmptyField, ExecutionException {


    if(user.getUserEmail() == null){
      throw new EmptyField();
    }

    UserCredentials user_sub = userCredentialsRepo.findByUserEmail(user.getUserEmail());
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

    if(user.getPassword().length() < 4) {
      throw new PasswordException();
    }


    if(user.getRole() == null){
      user.setRole("USER");
    }

    try{


      UserCredentials newUser = new UserCredentials(user.getUserEmail(), new BCryptPasswordEncoder().encode(user.getPassword()), user.getRole(), false);
      verificationMail.sendVerificationEmail(newUser);
      userCredentialsRepo.save(newUser);

      return new ResponseEntity<>(newUser.isVerified(),HttpStatus.OK);//Change response

    }
    catch (PatternSyntaxException p){
      return new ResponseEntity<>("invalid email", HttpStatus.BAD_REQUEST);
    }
    catch (Exception e){
      return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
  }

  public ResponseEntity<?> validateOtp(String userEmail, int otpEntered) throws ExecutionException {
      int otp = otpService.getOtp(userEmail);
      if(otpEntered == otp){
        userCredentialsRepo.verifyUser(userEmail);
        otpService.clearOtp(userEmail);
        return new ResponseEntity<>("Entered OTP is correct!", HttpStatus.ACCEPTED);
      }
      else{
        return new ResponseEntity<>("Entered OTP is wrong!", HttpStatus.BAD_REQUEST);
      }
  }

  public ResponseEntity<?> resendOtp(String userEmail) throws ExecutionException {

      if(userCredentialsRepo.findByUserEmail(userEmail) == null){
      return new ResponseEntity<>("Email not registered!", HttpStatus.NOT_FOUND);
    }

      otpService.clearOtp(userEmail);
      verificationMail.sendVerificationEmail(userService.findUserByEmail(userEmail));
      return new ResponseEntity<>("New OTP sent!", HttpStatus.OK);

  }


  public ResponseEntity<?> userDetails(String userEmail, String fullName, String username)
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

      UserCredentials user = userCredentialsRepo.findByUserEmail(userEmail);

    /*  if(image != null){
        Media media = new Media();
        fileUploadService.fileUpload(image, media.getMediaId().toString(), "instaPFP");
        user.setProfilePic(media);
        userRepository.save(user);
      }
      */

      UserProfile profile = new UserProfile(username, fullName, "PUBLIC", true);
      profile.setUser(user);
      profileRepository.save(profile);
      userCredentialsRepo.save(user);

      return new ResponseEntity<>(profile, HttpStatus.OK);
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
    return profileRepository.findByUsername(username) != null;
  }

  public boolean userEmailFound(String userEmail){
    return userCredentialsRepo.findByUserEmail(userEmail) != null;
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
      String username_regex = "^[a-z0-9._]{3,}$";
      Pattern pattern1 = Pattern.compile(username_regex);
      Matcher matcher1 = pattern1.matcher(username);

      if(!matcher1.matches()){
        throw new PatternSyntaxException("not a valid username.",username_regex,0);
      }

    }
}

