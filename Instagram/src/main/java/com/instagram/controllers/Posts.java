package com.instagram.controllers;


import com.instagram.serviceImpl.PostServiceImpl;
import com.instagram.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class Posts {

    @Autowired
    PostServiceImpl postService;

    @Autowired
    UserServiceImpl userService;

    @RequestMapping(method = RequestMethod.POST, path = "/uploadPost")
    public ResponseEntity<?> uploadPost(@RequestParam String token , @RequestParam List<MultipartFile> media, @RequestParam String location, @RequestParam String caption, @RequestParam boolean commentActivity) throws Exception {

        return postService.uploadPost(token, media, location, caption, commentActivity);
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/updatePost")
    public ResponseEntity<?> updatePost(@RequestBody Map<String, String > request){
        return postService.updatePost(request.get("token"), request.get("postId"), request.get("caption"));
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/pinPost")
    public ResponseEntity<?> pinPost(@RequestBody Map<String, String > request) {
        return postService.pinPost(request.get("token"), request.get("postId"));
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/unpinPost")
    public ResponseEntity<?> unpinPost(@RequestBody Map<String, String > request) {
        return postService.unpinPost(request.get("token"), request.get("postId"));

    }


        @RequestMapping(method = RequestMethod.DELETE, path = "/deletePost")
    public ResponseEntity<?> deletePost(@RequestBody Map<String, String> request){
            return postService.deletePost(request.get("token"), request.get("postId"));
    }
}
