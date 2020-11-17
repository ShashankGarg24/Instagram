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
    public ResponseEntity<?> uploadPost(@RequestParam String token , @RequestParam List<MultipartFile> media, @RequestParam String location, @RequestParam String caption) throws Exception {

        return postService.uploadPost(token, media, location, caption);
    }



    @RequestMapping(method = RequestMethod.PATCH, path = "/updatePost")
    public ResponseEntity<?> updatePost(@RequestBody Map<String, String > request){
        return postService.updatePost(request.get("token"), request.get("postId"), request.get("caption"));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/viewPost")
    public ResponseEntity<?> viewPost(@RequestBody Map<String, String > request){
        return postService.viewPost(request.get("postId"));
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

    @RequestMapping(method = RequestMethod.POST, path = "/createNewCategory")
    public ResponseEntity<?> createNewCategory(@RequestBody Map<String, String> request){
        return postService.createNewCategory(request.get("name"), request.get("postId"), request.get("token"));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/addToCategory")
    public ResponseEntity<?> addToCategory(@RequestBody Map<String, String> request){
        return postService.addToCategory(request.get("postId"), request.get("categoryId"), request.get("token"));

    }

    @RequestMapping(method = RequestMethod.POST, path = "/removeFromCategory")
    public ResponseEntity<?> removeFromCategory(@RequestBody Map<String, String> request){
        return postService.removeFromCategory(request.get("postId"), request.get("categoryId"), request.get("token"));

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/deleteCategory")
    public ResponseEntity<?> deleteCategory(@RequestBody Map<String, String> request){
        return postService.deleteCategory(request.get("categoryId"), request.get("token"));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/likeDislikePost")//ACCEPT BOOLEN VALUE
    public ResponseEntity<?> likeDislikePost(@RequestBody Map<String, String> request){
        return postService.likeDislikePost(request.get("postId"), request.get("token"));

    }

    @RequestMapping(method = RequestMethod.POST, path = "/addComment")
    public ResponseEntity<?> addComment(@RequestBody Map<String, String> request){
        return postService.addComment(request.get("postId"), request.get("token"), request.get("comment"));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getComments")
    public ResponseEntity<?> getCommentsByPostId(@RequestBody Map<String, String> request){
        return postService.getCommentsByPostId(request.get("postId"));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/deleteComment")
    public ResponseEntity<?> deleteComment(@RequestBody Map<String, String> request){
        return postService.deleteComment(request.get("postId"), request.get("commentId"), request.get("token"));
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/likeDislikeComment")
    public ResponseEntity<?> likeDislikeComment(@RequestBody Map<String, String> request){
        return postService.likeDislikeComment(request.get("commentId"), request.get("token"));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/addReply")
    public ResponseEntity<?> addReply(@RequestBody Map<String, String> request){
        return postService.addReply(request.get("commentId"), request.get("token"), request.get("reply"));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getReplies")
    public ResponseEntity<?> getRepliesByCommentId(@RequestBody Map<String, String> request){
        return postService.getRepliesByCommentId(request.get("commentId"));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/deleteReply")
    public ResponseEntity<?> deleteReply(@RequestBody Map<String, String> request){
        return postService.deleteReply(request.get("commentId"), request.get("replyId"), request.get("token"));
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/likeDislikeReply")
    public ResponseEntity<?> likeDislikeReply(@RequestBody Map<String, String> request){
        return postService.likeDislikeReply(request.get("replyId"), request.get("token"));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/uploadStory")
    public ResponseEntity<?> uploadStory(@RequestParam String token , @RequestParam List<MultipartFile> media) throws Exception {

        return postService.uploadStory(token, media);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getRecentPosts")
    public ResponseEntity<?> getPostsFromFollowing(@RequestBody Map<String, String> request){

        return postService.getPostsFromFollowing(request.get("token"));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getAllPostsByLikes")
    public ResponseEntity<?> getAllPostsByLikes(){

        return postService.getAllPostsByLikes();
    }
}
