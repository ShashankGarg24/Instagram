package com.instagram.services;


import com.instagram.Configuration.JwtUtil;
import com.instagram.models.*;
import com.instagram.repository.*;
import com.instagram.serviceImpl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    UserService userService;

    @Autowired
    MediaRepo mediaRepo;

    @Autowired
    CommentRepo commentRepo;

    @Autowired
    ReplyRepo replyRepo;

    @Autowired
    StoryRepo storyRepo;

    @Autowired
    USerLIkes userLikes;

    @Autowired
    JwtUtil jwtUtil;


    @Transactional
    public ResponseEntity<?> uploadPost(String token, List<MultipartFile> media, String location, String caption, List<String> taggedUserIds) throws Exception {

        try{
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            Posts post = new Posts(location, caption, true, false, profile.getProfilePicPath(), profile.getFullName(), profile.getUsername());
            post.setProfile(profile);
            postRepository.save(post);

            for(String id : taggedUserIds){
                UserProfile taggedUserProfile = profileRepository.findByProfileId(UUID.fromString(id));
                post.addToTaggedUsers(taggedUserProfile);
                taggedUserProfile.addToTaggedPosts(post);
                profileRepository.save(taggedUserProfile);
            }

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
            profile.addToPosts(post);
            profile.increasePostNumber();
            profileRepository.save(profile);
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
    public ResponseEntity<?> changeCommentActivity(String token, String postId) {
        try {
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            UUID postUUID = UUID.fromString(postId);
            Posts post = postRepository.findByPostId(postUUID);
            post.setPinned(false);
            postRepository.save(post);

            return new ResponseEntity<>("CommentActivity Changed!", HttpStatus.ACCEPTED);
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }



    @Transactional
    public ResponseEntity<?> deletePost(String token, String postId) {
        try {
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            UUID postUUID = UUID.fromString(postId);
            Media postMedia = mediaRepo.findByPostId(postUUID);
            profile.removePostMedia(postMedia);
            profile.removeFromPosts(postRepository.findByPostId(postUUID));
            mediaRepo.deleteById(postMedia.getMediaId());
            profileRepository.save(profile);
            for(LikeModel likeModel : userLikes.findAllByContentId(postUUID)){
                userLikes.delete(likeModel);
            }

            Posts post = postRepository.findByPostId(postUUID);

            for(UserProfile userProfile : post.getTaggedUsers()){
                userProfile.removeFromTaggedPosts(post);
                profileRepository.save(userProfile);
                post.removeFromTaggedUsers(userProfile);
            }
            postRepository.delete(post);
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

    @Transactional
    public ResponseEntity<?> likeDislikePost(String postId, String token, boolean likeStatus){
        try {
            Posts post = postRepository.findByPostId(UUID.fromString(postId));
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            LikeModel userLike = userLikes.findByProfileIdAndAndContentId(profile.getProfileId(), post.getPostId());
            if (userLike == null) {
                LikeModel like = new LikeModel(profile.getProfileId(), post.getPostId(), true);
                post.addUserLikes(like);
                post.increaseLikes();
                userLikes.save(like);
                postRepository.save(post);

                return new ResponseEntity<>("Post liked.", HttpStatus.ACCEPTED);
            }

            if(likeStatus == userLike.isLikeStatus()){
                return new ResponseEntity<>("post already liked/disliked", HttpStatus.valueOf(204));
            }

            userLike.setLikeStatus(likeStatus);
            userLikes.save(userLike);


            if(likeStatus){
                post.increaseLikes();
                postRepository.save(post);
                return new ResponseEntity<>("Post liked", HttpStatus.valueOf(205));
            }
            post.decreaseLikes();
            postRepository.save(post);
            return new ResponseEntity<>("Post disliked", HttpStatus.valueOf(205));

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Transactional
    public ResponseEntity<?> getCommentsByPostId(String postId){
        try {
            Posts post = postRepository.findByPostId(UUID.fromString(postId));

            return new ResponseEntity<>(post.getComments(), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional
    public ResponseEntity<?> deleteComment(String postId, String commentId, String token){
        try {
            Comment comment = commentRepo.findByCommentId(UUID.fromString(commentId));
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            System.out.println(profile.getUsername());
            System.out.println(comment.getParentUser().getUsername());

            if(!comment.getParentUser().equals(profile)){
                return new ResponseEntity<>("Only user who created the comment can delete it!", HttpStatus.EXPECTATION_FAILED);
            }

            Posts post = postRepository.findByPostId(UUID.fromString(postId));
            post.removeComment(comment);
            postRepository.save(post);

            for(LikeModel likeModel : userLikes.findAllByContentId(UUID.fromString(commentId))){
                userLikes.delete(likeModel);
            }
            commentRepo.delete(comment);

            return new ResponseEntity<>("comment successfully deleted.", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @Transactional
    public ResponseEntity<?> likeDislikeComment(String commentId, String token, boolean likeStatus){
        try {
            Comment comment = commentRepo.findByCommentId(UUID.fromString(commentId));
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            LikeModel userLike = userLikes.findByProfileIdAndAndContentId(profile.getProfileId(), comment.getCommentId());

            if (userLike == null) {
                LikeModel like = new LikeModel(profile.getProfileId(), comment.getCommentId(), true);
                comment.addUserLikes(like);
                comment.increaseLikes();
                userLikes.save(like);
                commentRepo.save(comment);

                return new ResponseEntity<>("Comment liked.", HttpStatus.ACCEPTED);
            }

            if(likeStatus == userLike.isLikeStatus()){
                return new ResponseEntity<>("comment already liked/disliked", HttpStatus.valueOf(204));
            }

            userLike.setLikeStatus(likeStatus);
            userLikes.save(userLike);

            if(likeStatus){
                comment.increaseLikes();
                commentRepo.save(comment);
                return new ResponseEntity<>("Comment liked.", HttpStatus.valueOf(202));
            }

            comment.decreaseLikes();
            commentRepo.save(comment);
            return new ResponseEntity<>("Comment disliked.", HttpStatus.valueOf(202));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @Transactional
    public ResponseEntity<?> addReply(String commentId, String token, String reply){
        try {
            Comment comment = commentRepo.findByCommentId(UUID.fromString(commentId));
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            Reply replyObject = new Reply(reply, profile);
            replyRepo.save(replyObject);
            comment.addToReplies(replyObject);
            comment.increaseReplyCount();
            commentRepo.save(comment);

            return new ResponseEntity<>("reply uploaded.", HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Transactional
    public ResponseEntity<?> getRepliesByCommentId(String commentId){
        try {
            Comment comment = commentRepo.findByCommentId(UUID.fromString(commentId));

            return new ResponseEntity<>(comment.getReplies(), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @Transactional
    public ResponseEntity<?> deleteReply(String commentId, String replyId,  String token){
        try {
            Reply reply = replyRepo.findByReplyId(UUID.fromString(replyId));
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));

            if(!reply.getParentUser().equals(profile)){
                return new ResponseEntity<>("Only user who created the reply can delete it!", HttpStatus.EXPECTATION_FAILED);
            }

            Comment comment = commentRepo.findByCommentId(UUID.fromString(commentId));
            comment.removeFromReplies(reply);
            comment.decreaseReplyCount();
            commentRepo.save(comment);
            for(LikeModel likeModel : userLikes.findAllByContentId(UUID.fromString(replyId))){
                userLikes.delete(likeModel);
            }
            replyRepo.delete(reply);

            return new ResponseEntity<>("reply successfully deleted.", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @Transactional
    public ResponseEntity<?> likeDislikeReply(String replyId, String token, boolean likeStatus){
        try {
            Reply reply = replyRepo.findByReplyId(UUID.fromString(replyId));
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            LikeModel userLike = userLikes.findByProfileIdAndAndContentId(profile.getProfileId(), reply.getReplyId());
            if (userLike == null) {
                LikeModel like = new LikeModel(profile.getProfileId(), reply.getReplyId(), true);
                reply.addUserLikes(like);
                reply.increaseLikes();
                userLikes.save(like);
                replyRepo.save(reply);

                return new ResponseEntity<>("Reply liked.", HttpStatus.ACCEPTED);
            }

            if(likeStatus == userLike.isLikeStatus()){
                return new ResponseEntity<>("reply already liked/disliked", HttpStatus.valueOf(204));
            }

            userLike.setLikeStatus(likeStatus);
            userLikes.save(userLike);

            if(likeStatus){
                reply.increaseLikes();
                replyRepo.save(reply);
                return new ResponseEntity<>("Reply liked.", HttpStatus.valueOf(202));

            }

            reply.decreaseLikes();
            replyRepo.save(reply);
            return new ResponseEntity<>("Reply disliked.", HttpStatus.valueOf(202));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional
    public ResponseEntity<?> uploadStory(String token, List<MultipartFile> media){

        try{
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            Story story = new Story(profile);

            for (MultipartFile file : media) {
                String path = fileUploadService.fileUpload(file, story.getStoryId().toString(), "instaStories");
                story.setStoryMediaPath(path);
            }

            storyRepo.save(story);
           // profile.increaseStoryNumber();

            return new ResponseEntity<>("Story Uploaded!", HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteStory(String token, String storyId) {
        try {
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            storyRepo.deleteById(UUID.fromString(storyId));
         //   profile.decreaseStoryNumber();

            return new ResponseEntity<>("Story Deleted!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Async
    @Scheduled(fixedRate = 1000)
    public void automaticStoryDeletion(){

    }

    public ResponseEntity<?> getPostsFromFollowing(String token){
        try{
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            List<Posts> posts = new ArrayList<>();
            List<UserProfile> followings = profile.getFollowing();
            if(followings.isEmpty()){
                return new ResponseEntity<>(userService.getSuggestedUsers(token), HttpStatus.valueOf(202));
            }
            System.out.println(11);
            for (UserProfile profile1 : followings) {
                if (profile1.getPostNumber() != 0) {
                    List<Posts> userPosts = postRepository.findAllByProfileProfileId(profile1.getProfileId());
                    for (Posts post : userPosts) {
                        posts.add(post);
                    }
                }
            }

            System.out.println(11);
            if(posts.isEmpty()){
                return new ResponseEntity<>(userService.getSuggestedUsers(token), HttpStatus.valueOf(202));
            }
            System.out.println(11);
           //Comparator<PostDTO> compareByDateOfCreation = Comparator.comparing(Posts::getpostCreationTimeStamp);
           //
            //Collections.sort(posts, compareByDateOfCreation);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> getAllPostsByLikes(){
       try{
            List<Posts> allPublicPosts = postRepository.findAllByProfileUserPrivacy("PUBLIC");
            Comparator<Posts> compareByLikes = Comparator.comparing(Posts::getLikes);
            Collections.sort(allPublicPosts, compareByLikes.reversed());
            return new ResponseEntity<>(allPublicPosts, HttpStatus.OK);
        }
        catch (Exception e){
           return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

}