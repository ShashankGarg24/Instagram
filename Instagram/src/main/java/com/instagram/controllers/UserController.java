package com.instagram.controllers;


import com.instagram.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

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
