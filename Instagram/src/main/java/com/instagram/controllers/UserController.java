package com.instagram.controllers;


import com.instagram.models.ResetDetails;
import com.instagram.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
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
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> userDetail){
        return userService.forgotPassword(userDetail.get("detail"));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/setNewPassword")
    public ResponseEntity<?> setNewPassword(@RequestBody Map<String, String> response){
        return userService.setNewPassword(response.get("email"), response.get("password"));
    }


    @RequestMapping(method = RequestMethod.DELETE, path = "/deleteProfilePic")
     public ResponseEntity<?> deleteProfilePic(@RequestBody Map<String , String > request){
        try{
            return userService.deleteProfilePic(request.get("token"));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
     }

    @RequestMapping(method = RequestMethod.PATCH, path = "/updateProfilePic")
    public ResponseEntity<?> updateProfilePic(@RequestParam("image") MultipartFile image, @RequestParam("token") String token){
        try{
            return userService.updateProfilePic(token, image);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String , String > request){
        try{
            return userService.updateProfile(request.get("token"), request.get("name"), request.get("username"), request.get("bio"), request.get("birthDate"), request.get("privacy"));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/follow")
    public ResponseEntity<?> follow(@RequestBody Map<String , String > request){

            return userService.follow(request.get("token"), request.get("userId"));
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/unfollow")
    public ResponseEntity<?> unfollow(@RequestBody Map<String , String > request){

        return userService.unfollow(request.get("token"), request.get("userId"));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/searchUser")
    public ResponseEntity<?> searchUser(@RequestBody Map<String , String > request){

        return userService.searchUser(request.get("keyword"));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getSuggestedUsers")
    public ResponseEntity<?> getSuggestedUsers(@RequestBody Map<String , String > request) {

        return userService.getSuggestedUsers(request.get("token"));
    }

    }
