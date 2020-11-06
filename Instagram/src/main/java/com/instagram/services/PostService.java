package com.instagram.services;


import com.instagram.Configuration.JwtUtil;
import com.instagram.models.Media;
import com.instagram.models.Posts;
import com.instagram.models.UserProfile;
import com.instagram.repository.MediaRepo;
import com.instagram.repository.PostRepository;
import com.instagram.repository.ProfileRepository;
import com.instagram.serviceImpl.PostServiceImpl;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ProfileRepository profileRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    FileDeletingService fileDeletingService;

    @Autowired
    MediaRepo mediaRepo;

    @Autowired
    JwtUtil jwtUtil;



    public ResponseEntity<?> uploadPost(String token, List<MultipartFile> media, String location, String caption, boolean commentActivity) throws Exception {

        try{
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            Posts post = new Posts(location, caption, commentActivity);

            for (MultipartFile file : media) {
                Media postMedia = new Media();
                String path = fileUploadService.fileUpload(file, postMedia.getMediaId().toString(), "instaPosts");
                postMedia.setMediaPath(path);
                postMedia.setPinned(false);
                mediaRepo.save(postMedia);
                post.addMedia(postMedia);
                profile.addPostMedia(postMedia);
            }
            postRepository.save(post);
            profile.addPost(post);
            profileRepository.save(profile);

            return new ResponseEntity<>("Post Updated!", HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

   @Transactional
    public void deletePost(UUID postId) {
        Posts post = postRepository.findByPostId(postId);
        List<Media> Media = post.getMedia();
        for (Media p : Media) {
            fileDeletingService.deleteFile(p.getMediaId().toString(), "instaPosts");
           mediaRepo.deleteById(p.getMediaId());
        }
        postRepository.delete(post);
    }


}
