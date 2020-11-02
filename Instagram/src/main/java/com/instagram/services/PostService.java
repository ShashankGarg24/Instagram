package com.instagram.services;


import com.instagram.models.PostMedia;
import com.instagram.models.Posts;
import com.instagram.repository.PostMediaRepo;
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
    PostMediaRepo postMediaRepo;

    public void uploadPost(String username, List<MultipartFile> media, String location, String caption, boolean commentActivity)throws Exception{

        Posts post = new Posts(location, caption, commentActivity);
        postRepository.save(post);
        for (MultipartFile m : media){
            PostMedia postMedia = new PostMedia();
            fileUploadService.fileUpload(m, postMedia.getMediaId().toString(), "instaPosts");
            postMedia.set_post(post);
            postMediaRepo.save(postMedia);
        }

    }

    @Transactional
    public void deletePost(UUID postId){
        Posts post = postRepository.findByPostId(postId);
        List<PostMedia> postMedia = post.getPostMedias();
        for (PostMedia p : postMedia) {
            fileDeletingService.deleteFile(p.getMediaId().toString(), "instaPosts");
            postMediaRepo.deleteById(p.getMediaId());
        }
        postRepository.delete(post);
    }



}
