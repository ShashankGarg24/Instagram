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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    EntityManager e;

    @Autowired
    MediaRepo mediaRepo;

    @Autowired
    JwtUtil jwtUtil;


    @Transactional
    public ResponseEntity<?> uploadPost(String token, List<MultipartFile> media, String location, String caption, boolean commentActivity) throws Exception {

        try{
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            Posts post = new Posts(location, caption, commentActivity);
            post.setProfile(profile);
            postRepository.save(post);

            for (MultipartFile file : media) {
                Media postMedia = new Media();
                String path = fileUploadService.fileUpload(file, postMedia.getMediaId().toString(), "instaPosts");
                postMedia.setData(path, false, post.getPostId(), post);
                mediaRepo.save(postMedia);
                profile.addPostMedia(postMedia);
                profileRepository.save(profile);
            }
            profile.increasePostNumber();
            return new ResponseEntity<>("Post Uploaded! " + post.getPostId(), HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> updatePost(String token, String postId, String caption) {
        try {
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            Posts post = postRepository.findByPostId(UUID.fromString(postId));
            post.setCaption(caption);
            post.setPostLastUpdateTimeStamp(LocalDateTime.now());
            postRepository.save(post);
            return new ResponseEntity<>("Post Updated!", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> unpinPost(String token, String postId) {
        try {
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            Posts post = postRepository.findByPostId(UUID.fromString(postId));
            for (Media postMedia : profile.getPostMedia()) {
                if(postMedia.getPostId().equals(post.getPostId())) {
                    postMedia.setPinned(false);
                    mediaRepo.save(postMedia);
                }
            }
            return new ResponseEntity<>("Post unpinned!", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);

        }
    }

    public ResponseEntity<?> pinPost(String token, String postId) {
        try {
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));

            for (Media media : profile.getPostMedia()) {
                if(media.isPinned()){
                    media.setPinned(false);
                    mediaRepo.save(media);
                }
            }
            UUID postUUID = UUID.fromString(postId);
            for (Media media : mediaRepo.findAllByPostId(postUUID)) {
                media.setPinned(true);
                mediaRepo.save(media);
            }
            return new ResponseEntity<>("Post pinned!", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @Transactional
    public ResponseEntity<?> deletePost(String token, String postId) {
        try {
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            UUID postUUID = UUID.fromString(postId);
            for(Media postMedia : mediaRepo.findAllByPostId(postUUID)){
                profile.removePostMedia(postMedia);
                mediaRepo.deleteById(postMedia.getMediaId());
            }
            profileRepository.save(profile);
            postRepository.deleteById(postUUID);
            profile.decreasePostNumber();
            return new ResponseEntity<>("Post Deleted!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

}