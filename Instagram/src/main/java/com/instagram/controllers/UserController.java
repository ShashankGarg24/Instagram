package com.instagram.controllers;


import com.instagram.Configuration.JwtUtil;
import com.instagram.DTO.UserShortDetailsDTO;
import com.instagram.models.ResetDetails;
import com.instagram.models.Response;
import com.instagram.models.User;
import com.instagram.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    JwtUtil jwtUtil;

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
    public List<UserShortDetailsDTO> getSuggestedUsers(@RequestBody Map<String , String > request) {

        return userService.getSuggestedUsers(request.get("token"));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getFollowers")
    public ResponseEntity<?> getFollowers(@RequestHeader(name = "Authorization") String token) {

        return userService.getFollowers(token.substring(7));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getFollowing")
    public ResponseEntity<?> getFollowing(@RequestHeader(name = "Authorization") String token) {

        return userService.getFollowing(token.substring(7));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/getAccessToken")
    public ResponseEntity<?> getAccessToken(@RequestBody Map<String, String> request){
        return new ResponseEntity<>(new Response(jwtUtil.generateToken(jwtUtil.getUsernameFromToken(request.get("token")))), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getProfileFromToken/{token}")
    public ResponseEntity<?> getProfileFromToken(@PathVariable("token") String token){
        return userService.getProfileFromToken(token);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/viewProfile/{profileId}")
    public ResponseEntity<?> viewProfile(@RequestHeader(name = "Authorization") String token, @PathVariable("profileId") String profileId){

       return userService.viewProfile(token.substring(7), profileId);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/acceptRequest")
    public ResponseEntity<?> acceptRequest(@RequestHeader(name = "Authorization") String token, @RequestBody Map<String, String> request){
        return userService.acceptRequest(token.substring(7), request.get("userId"));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/declineRequest")
    public ResponseEntity<?> declineRequest(@RequestHeader(name = "Authorization") String token, @RequestBody Map<String, String> request){
        return userService.declineRequest(token.substring(7), request.get("userId"));
    }
}
