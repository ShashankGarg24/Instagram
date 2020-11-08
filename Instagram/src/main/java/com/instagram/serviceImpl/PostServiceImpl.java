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
}
