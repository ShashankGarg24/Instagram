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

    @RequestMapping(method = RequestMethod.DELETE, path = "/deletePost/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") String postId){
        try{
            UUID id = userService.convertToUUID(postId);
            postService.deletePost(id);
            return new ResponseEntity<>("Post Deleted!", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
