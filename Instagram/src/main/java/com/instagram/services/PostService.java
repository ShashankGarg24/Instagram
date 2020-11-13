package com.instagram.services;


import com.instagram.Configuration.JwtUtil;
import com.instagram.DTO.PostDTO;
import com.instagram.models.*;
import com.instagram.repository.*;
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
import java.util.*;

@Service
public class PostService implements PostServiceImpl {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    CustomCategoryRepo customCategoryRepo;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    FileDeletingService fileDeletingService;

    @Autowired
    MediaRepo mediaRepo;

    @Autowired
    CommentRepo commentRepo;

    @Autowired
    USerLIkes userLikes;

    @Autowired
    JwtUtil jwtUtil;


    @Transactional
    public ResponseEntity<?> uploadPost(String token, List<MultipartFile> media, String location, String caption, boolean commentActivity) throws Exception {

        try{
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            Posts post = new Posts(location, caption, commentActivity, false);
            post.setProfile(profile);
            postRepository.save(post);

            List<String> allPath = new ArrayList<>();
            Media postMedia = new Media(false, post.getPostId());
            for (MultipartFile file : media) {
                String path = fileUploadService.fileUpload(file, postMedia.getMediaId().toString(), "instaPosts");
                allPath.add(path);
            }

            postMedia.setmediaPath(allPath);
            mediaRepo.save(postMedia);
            post.setPostMedia(postMedia);
            postRepository.save(post);
            profile.addPostMedia(postMedia);
            profileRepository.save(profile);
            profile.increasePostNumber();
            return new ResponseEntity<>("Post Uploaded! " + post.getPostId(), HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> viewPost(String postId){
        try{
            Posts post = postRepository.findByPostId(UUID.fromString(postId));
            if(post == null){
                return new ResponseEntity<>("no such post available!", HttpStatus.BAD_REQUEST);
            }

            //Media postMedia = mediaRepo.findByPostId(post.getPostId());
            //PostDTO postDTO = new PostDTO(post, postMedia);
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(402));
        }
    }

    @Transactional
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

    @Transactional
    public ResponseEntity<?> unpinPost(String token, String postId) {
        try {
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            UUID postUUID = UUID.fromString(postId);
            Posts post = postRepository.findByPostId(postUUID);
            post.setPinned(false);
            postRepository.save(post);
            Media postMedia = mediaRepo.findByPostId(postUUID);
            postMedia.setPinned(false);
            mediaRepo.save(postMedia);

            return new ResponseEntity<>("Post unpinned!", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);

        }
    }

    @Transactional
    public ResponseEntity<?> pinPost(String token, String postId) {
        try {
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));

            for (Media media : profile.getPostMedia()) {
                if(media.isPinned()){
                    media.setPinned(false);
                    mediaRepo.save(media);
                    Posts post = postRepository.findByPostId(media.getPostId());
                    post.setPinned(false);
                    postRepository.save(post);
                    break;

                }
            }

            UUID postUUID = UUID.fromString(postId);
            Posts postToBePinned = postRepository.findByPostId(postUUID);
            Media media = mediaRepo.findByPostId(postUUID);
            media.setPinned(true);
            mediaRepo.save(media);

            postToBePinned.setPinned(true);
            postRepository.save(postToBePinned);
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
            Media postMedia = mediaRepo.findByPostId(postUUID);
            profile.removePostMedia(postMedia);
            mediaRepo.deleteById(postMedia.getMediaId());
            profileRepository.save(profile);
            postRepository.deleteById(postUUID);
            profile.decreasePostNumber();
            return new ResponseEntity<>("Post Deleted!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Transactional
    public ResponseEntity<?> createNewCategory(String name, String postId, String token){
        try{
            Posts post = postRepository.findByPostId(UUID.fromString(postId));
            Media postMedia = post.getPostMedia();
            String path = postMedia.getMediaPath().get(0);
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            if(profile.getCategoryNumber() == 0){
                CustomCategory primaryCategory = new CustomCategory(true, path, "All Posts");
                primaryCategory.addSavedPosts(postMedia);
                primaryCategory.increaseSavedPostsNumber();
                customCategoryRepo.save(primaryCategory);
                profile.increaseCategoryNumber();
                profile.addCategories(primaryCategory);
                profileRepository.save(profile);
            }

            CustomCategory newCategory = new CustomCategory(false, path, name);
            newCategory.addSavedPosts(postMedia);
            newCategory.increaseSavedPostsNumber();
            customCategoryRepo.save(newCategory);
            profile.increaseCategoryNumber();
            profile.addCategories(newCategory);
            profileRepository.save(profile);

            return new ResponseEntity<>("category created", HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Transactional
    public ResponseEntity<?> addToCategory(String postId, String categoryId, String token){
        try{
            CustomCategory category = customCategoryRepo.findByCategoryId(UUID.fromString(categoryId));
            Posts post = postRepository.findByPostId(UUID.fromString(postId));
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            CustomCategory primaryCategory = null;
            for(CustomCategory c : profile.getCategories()){
                if(c.isPrimaryType()){
                    primaryCategory = c;
                    break;
                }
            }

            primaryCategory.addSavedPosts(post.getPostMedia());
            primaryCategory.increaseSavedPostsNumber();
            category.addSavedPosts(post.getPostMedia());
            category.increaseSavedPostsNumber();
            customCategoryRepo.save(category);
            customCategoryRepo.save(primaryCategory);

            return new ResponseEntity<>("post saved!", HttpStatus.ACCEPTED);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Transactional
    public ResponseEntity<?> removeFromCategory(String postId, String categoryId, String token){
        try{
            CustomCategory category = customCategoryRepo.findByCategoryId(UUID.fromString(categoryId));
            Posts post = postRepository.findByPostId(UUID.fromString(postId));
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            CustomCategory primaryCategory = null;
            for(CustomCategory c : profile.getCategories()){
                if(c.isPrimaryType()){
                    primaryCategory = c;
                }
            }
            primaryCategory.removeSavedPosts(post.getPostMedia());
            primaryCategory.decreaseSavedPostsNumber();
            category.removeSavedPosts(post.getPostMedia());
            category.decreaseSavedPostsNumber();
            customCategoryRepo.save(category);
            customCategoryRepo.save(primaryCategory);

            return new ResponseEntity<>("post removed!", HttpStatus.ACCEPTED);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteCategory(String categoryId, String token){
        try{
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            CustomCategory category = customCategoryRepo.findByCategoryId(UUID.fromString(categoryId));
            profile.removeCategories(category);
            profile.decreaseCategoryNumber();
            profileRepository.save(profile);
            customCategoryRepo.deleteById(category.getCategoryId());

            return new ResponseEntity<>("category deleted!", HttpStatus.ACCEPTED);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> likeDislikePost(String postId, String token){
        try {
            Posts post = postRepository.findByPostId(UUID.fromString(postId));
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            LikeModel userLike = userLikes.findByProfileIdAndAndContentId(profile.getProfileId(), post.getPostId());
            if (userLike == null) {
                LikeModel like = new LikeModel(profile.getProfileId(), post.getPostId());
                post.addUserLikes(like);
                post.increaseLikes();
                userLikes.save(like);
                postRepository.save(post);

                return new ResponseEntity<>("Post liked.", HttpStatus.ACCEPTED);
            }

            post.removeUserLikes(userLike);
            post.decreaseLikes();
            postRepository.save(post);
            userLikes.deleteById(userLike.getId());

            return new ResponseEntity<>("Post disliked.", HttpStatus.valueOf(205));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> addComment(String postId, String token, String comment){
        try {
            Posts post = postRepository.findByPostId(UUID.fromString(postId));
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            Comment commentObject = new Comment(comment, profile);
            commentRepo.save(commentObject);
            post.addComment(commentObject);
            postRepository.save(post);

            return new ResponseEntity<>("comment uploaded.", HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getCommentsByPostId(String postId){
        try {
            Posts post = postRepository.findByPostId(UUID.fromString(postId));

            return new ResponseEntity<>(post.getComments(), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> deleteComment(String postId, String commentId, String token){
        try {
            Comment comment = commentRepo.findByCommentId(UUID.fromString(commentId));
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));

            if(!comment.getParentUser().equals(profile)){
                return new ResponseEntity<>("Only user who created the comment can delete it!", HttpStatus.EXPECTATION_FAILED);
            }

            Posts post = postRepository.findByPostId(UUID.fromString(postId));
            post.removeComment(comment);
            postRepository.save(post);
            commentRepo.delete(comment);

            return new ResponseEntity<>("comment successfully deleted.", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> likeDislikeComment(String commentId, String token){
        try {
            Comment comment = commentRepo.findByCommentId(UUID.fromString(commentId));
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            LikeModel userLike = userLikes.findByProfileIdAndAndContentId(profile.getProfileId(), comment.getCommentId());
            if (userLike == null) {
                LikeModel like = new LikeModel(profile.getProfileId(), comment.getCommentId());
                comment.addUserLikes(like);
                comment.increaseLikes();
                userLikes.save(like);
                commentRepo.save(comment);

                return new ResponseEntity<>("Comment liked.", HttpStatus.ACCEPTED);
            }

            comment.removeUserLikes(userLike);
            comment.decreaseLikes();
            commentRepo.save(comment);
            userLikes.deleteById(userLike.getId());

            return new ResponseEntity<>("Comment disliked.", HttpStatus.valueOf(202));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}