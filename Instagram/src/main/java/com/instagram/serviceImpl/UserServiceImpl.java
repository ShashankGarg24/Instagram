package com.instagram.serviceImpl;

import com.instagram.models.ResetDetails;
import com.instagram.models.User;
import com.instagram.models.UserCredentials;
import com.instagram.models.UserProfile;
import org.checkerframework.common.value.qual.StringVal;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


@Service
public interface UserServiceImpl {

    UserCredentials findUserByEmail(String userEmail);

    UserProfile findUserByUsername(String username);

    UUID convertToUUID(String userId);

    ResponseEntity<?> updatePrivacy(String username, String privacy);

    ResponseEntity<?> deleteProfilePic(String token);

    ResponseEntity<?> updateProfile(String token, String name, String username, String userBio, String birthDate, String profilePrivacy) throws Exception;

    ResponseEntity<?> updateProfilePic(String token, MultipartFile image) throws Exception;

    ResponseEntity<?> resetPassword(ResetDetails userDetails);

    ResponseEntity<?> setNewPassword(String userEmail, String password);

    ResponseEntity<?> forgotPassword(String userEmail);

    void updateUser(UserCredentials user);

    ResponseEntity<?> follow(String token, String userId);

    ResponseEntity<?> unfollow(String token, String userId);

    ResponseEntity<?> searchUser(String keyword);

    ResponseEntity<?> getSuggestedUsers(String token);
}
