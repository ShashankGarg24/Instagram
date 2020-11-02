package com.instagram.serviceImpl;

import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.List;
import java.util.UUID;

public interface PostServiceImpl {

    void uploadPost(String username, List<MultipartFile> media, String location, String caption, boolean commentActivity) throws Exception;

    void deletePost(UUID postId);
}
