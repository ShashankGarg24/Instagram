package com.instagram.controllers;

import com.instagram.Exceptions.EmptyField;
import com.instagram.Exceptions.PasswordException;
import com.instagram.Exceptions.UserEmailAlreadyExist;
import com.instagram.Exceptions.UsernameAlreadyExist;
import com.instagram.models.SignUp;
import com.instagram.models.User;
import com.instagram.serviceImpl.RegistrationImpl;
import com.instagram.serviceImpl.UserServiceImpl;
import com.instagram.services.Registration;
import com.sun.mail.iap.Response;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.PatternSyntaxException;
import javax.management.BadAttributeValueExpException;
import jdk.jfr.Registered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HttpCodeStatusMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UserRegistration {

  @Autowired
  RegistrationImpl registration;

  @Autowired
  UserServiceImpl userService;

  @RequestMapping(method = RequestMethod.POST, path = "/registration")
  public ResponseEntity<?> userRegistration(@RequestBody Map<String, String> user) {
    try {
      SignUp signUp = new SignUp(user.get("userEmail"), user.get("password"), user.get("role"));
      return this.registration.registerUser(signUp);
    } catch (PatternSyntaxException p) {
      return new ResponseEntity<>("invalid email address", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(method = RequestMethod.POST, path = "/validateOTP")
  public ResponseEntity<?> validateOtp(@RequestBody Map<String, String> body) throws ExecutionException{
    try {
      return registration.validateOtp(body.get("email"), Integer.parseInt(body.get("otp")));
    }
    catch (Exception e){
      return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @RequestMapping(method = RequestMethod.POST, path = "/resendOTP/{email}")
  public ResponseEntity<?> resendOtp(@PathVariable("email") String userEmail) throws ExecutionException{
    return registration.resendOtp(userEmail);
  }

  @RequestMapping(method = RequestMethod.POST, path = "/details")
  public ResponseEntity<?> userProfileDetails(@RequestBody Map<String, String> details){

    System.out.println(details.get("email"));
    System.out.println(details.get("name"));
    System.out.println(details.get("username"));


    if(!userService.findUserByEmail(details.get("email")).isVerified()){
      return new ResponseEntity<>("Unverified user!", HttpStatus.BAD_REQUEST);
    }

    try{
      return this.registration.userDetails(details.get("email"), details.get("name"), details.get("username"));
    }
    catch (UsernameAlreadyExist usernameAlreadyExist){
      List<String> suggestions = registration.suggestions(details.get("username"));
      return new ResponseEntity<>(suggestions, HttpStatus.ACCEPTED);
    }
    catch (PatternSyntaxException patternSyntaxException){
      return new ResponseEntity<>("invalid username", HttpStatus.BAD_REQUEST);
    }
    catch (Exception e){
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(method = RequestMethod.POST, path = "/usernameSuggestions")
  public ResponseEntity<?> SuggestUsernames(@RequestParam("username") String username){
    try{
      if(!registration.usernameFound(username)){
        return new ResponseEntity<>("unique username", HttpStatus.OK);
      }
      List<String> suggestions = registration.suggestions(username);
      return new ResponseEntity<>(suggestions,HttpStatus.OK);
    }
    catch (Exception e){
      return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }
}
