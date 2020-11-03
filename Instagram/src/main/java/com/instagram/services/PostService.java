package com.instagram.services;


import com.instagram.models.Media;
import com.instagram.models.Posts;
import com.instagram.repository.MediaRepo;
import com.instagram.repository.PostRepository;
import com.instagram.serviceImpl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.List;
import java.util.UUID;

@Service
public class PostService implements PostServiceImpl {

    @Autowired
    PostRepository postRepository;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    FileDeletingService fileDeletingService;

    @Autowired
    MediaRepo MediaRepo;

    public void uploadPost(String username, List<MultipartFile> media, String location, String caption, boolean commentActivity)throws Exception{

        Posts post = new Posts(location, caption, commentActivity);
        postRepository.save(post);
        for (MultipartFile m : media){
            Media Media = new Media();
            fileUploadService.fileUpload(m, Media.getMediaId().toString(), "instaPosts");
            Media.set_post(post);
            MediaRepo.save(Media);
        }

    }

    @Transactional
    public void deletePost(UUID postId){
        Posts post = postRepository.findByPostId(postId);
        List<Media> Media = post.getMedia();
        for (Media p : Media) {
            fileDeletingService.deleteFile(p.getMediaId().toString(), "instaPosts");
            MediaRepo.deleteById(p.getMediaId());
        }
        postRepository.delete(post);
    }



}
