package com.instagram.serviceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface PostServiceImpl {

    ResponseEntity uploadPost(String token, List<MultipartFile> media, String location, String caption, boolean commentActivity) throws Exception;

    ResponseEntity<?> updatePost(String token, String postId, String caption);

    ResponseEntity<?> unpinPost(String token, String postId);

    ResponseEntity<?> pinPost(String token, String postId);

    ResponseEntity<?> deletePost(String token, String postId);

    ResponseEntity<?> viewPost(String postId);

    ResponseEntity<?> createNewCategory(String name, String postId, String token);

    ResponseEntity<?> addToCategory(String postId, String categoryId, String token);

    ResponseEntity<?> removeFromCategory(String postId, String categoryId, String token);

    ResponseEntity<?> deleteCategory(String categoryId, String token);

    ResponseEntity<?> likeDislikePost(String postId, String token);

    ResponseEntity<?> addComment(String postId, String token, String comment);

    ResponseEntity<?> getCommentsByPostId(String postId);

    ResponseEntity<?> deleteComment(String postId, String commentId, String token);

    ResponseEntity<?> likeDislikeComment(String commentId, String token);

    ResponseEntity<?> addReply(String commentId, String token, String reply);

    ResponseEntity<?> getRepliesByCommentId(String commentId);

    ResponseEntity<?> deleteReply(String commentId, String replyId, String token);

    ResponseEntity<?> likeDislikeReply(String replyId, String token);
}