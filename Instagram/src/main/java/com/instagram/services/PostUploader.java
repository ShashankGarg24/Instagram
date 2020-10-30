package com.instagram.services;


import com.instagram.models.Posts;
import com.instagram.repository.PostRepository;
import com.instagram.serviceImpl.PostUploaderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostUploader implements PostUploaderImpl {

    @Autowired
    PostRepository postRepository;

    public void uploadPost(String username, String location, String caption, boolean commentActivity){

        Posts post = new Posts(location, caption, commentActivity);
        postRepository.save(post);
    }


}
