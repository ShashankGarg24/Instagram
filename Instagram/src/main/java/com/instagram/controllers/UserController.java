package com.instagram.controllers;


import com.instagram.models.ResetDetails;
import com.instagram.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @RequestMapping(method = RequestMethod.POST, path = "/modifyPrivacy/{username}")
    public ResponseEntity<?> updatePrivacy(@PathVariable("username") String username, @RequestParam String privacy){
        try{
            return userService.updatePrivacy(username, privacy);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetDetails userDetails){
        return userService.resetPassword(userDetails);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String userEmail){
        return userService.forgotPassword(userEmail);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/validateOtp/{userEmail}")
    public ResponseEntity<?> validateOtp(@PathVariable("userEmail") String userEmail,@RequestParam("otp") String otpEntered) throws ExecutionException{
        try {
            return userService.validateOtp(userEmail, Integer.parseInt(otpEntered));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/resendOtp")
    public ResponseEntity<?> resendOtp(@RequestParam("email") String userEmail) throws ExecutionException {
        return userService.resendOtp(userEmail);
    }



    @RequestMapping(method = RequestMethod.DELETE, path = "/deleteProfilePic/{userId}")
     public ResponseEntity<?> deleteProfilePic(@PathVariable("userId") String userId){
        try{
            UUID id = userService.convertToUUID(userId);
            return userService.deleteProfilePic(id);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
     }

    @RequestMapping(method = RequestMethod.PATCH, path = "/updateProfilePic/{userId}")
    public ResponseEntity<?> updateProfilePic(@RequestParam("image") MultipartFile image, @PathVariable("userId") String userId){
        try{
            UUID id = userService.convertToUUID(userId);
            return userService.updateProfilePic(image, id);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
