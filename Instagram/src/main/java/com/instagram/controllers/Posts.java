package com.instagram.controllers;


import com.instagram.serviceImpl.PostUploaderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.Multipart;

@RestController
@RequestMapping("/api")
public class Posts {

    @Autowired
    PostUploaderImpl postUploader;

    @RequestMapping(method = RequestMethod.POST, path = "/uploadPost/{username}")
    public ResponseEntity<?> uploadPost(@PathVariable String username ,@RequestParam String location, @RequestParam String caption, @RequestParam boolean commentActivity) {
        try{
            postUploader.uploadPost(username, location, caption, commentActivity);
            return new ResponseEntity<>("Post uploaded succesfully!", HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
