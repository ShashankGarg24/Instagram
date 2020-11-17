package com.instagram.services;

import com.instagram.Configuration.JwtUtil;
import com.instagram.DTO.UserShortDetailsDTO;
import com.instagram.controllers.Login;
import com.instagram.models.*;
import com.instagram.repository.MediaRepo;
import com.instagram.repository.ProfileRepository;
import com.instagram.repository.UserCredentialsRepo;
import com.instagram.repository.UserRepository;
import com.instagram.serviceImpl.UserServiceImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.hibernate.jdbc.Expectation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService implements UserServiceImpl {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserCredentialsRepo userCredentialsRepo;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MediaRepo mediaRepo;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    FileDeletingService fileDeletingService;

    @Autowired
    MailService mailService;

    @Autowired
    OtpService otpService;

    public UserCredentials findUserByEmail(String userEmail) {
        return userCredentialsRepo.findByUserEmail(userEmail);
    }

    public User findUserByToken(String userToken) {
        return userRepository.findByVerificationToken(userToken);
    }

    public UserProfile findUserByUsername(String username) {
        return profileRepository.findByUsername(username);
    }

    public User findUserByUserId(UUID userId) {

        return userRepository.findByUserId(userId);
    }

    public void updateUser(UserCredentials user) {
        userCredentialsRepo.save(user);
    }

    public ResponseEntity<?> updatePrivacy(String username, String privacy) {
        UserProfile user = findUserByUsername(username);
        if (!user.getUserPrivacy().equals(privacy)) {
            userRepository.updatePrivacy(privacy, username);
            return new ResponseEntity<>(username + " account set to " + privacy, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(username + " account is already set to " + privacy, HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteProfilePic(String token) {
        UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
        if (profile.getProfilePicPath() == null) {
            return new ResponseEntity<>("Profile pic not set", HttpStatus.EXPECTATION_FAILED);
        }

        fileDeletingService.deleteFile(profile.getProfileId().toString(), "instaPFP");
        profile.setProfilePicPath(null);
        profileRepository.save(profile);
        return new ResponseEntity<>("Profile pic removed!", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateProfilePic(String token, MultipartFile image) throws Exception {
        UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));

        if(profile.getProfilePicPath() != null){
            deleteProfilePic(token);
        }

        String path = fileUploadService.fileUpload(image, profile.getProfileId().toString(), "instaPFP");
        profile.setProfilePicPath(path);
        profileRepository.save(profile);

        return new ResponseEntity<>("Profile pic updated!", HttpStatus.OK);
    }

    @Transactional
  public ResponseEntity<?> updateProfile(String token, String name, String username, String userBio, String birthDate, String profilePrivacy) throws Exception {
      try{
          int flag = 0;
          UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
          profile.setFullName(name);
          if(!username.equals(profile.getUsername())){
              flag = 1;
          }
          profile.setUsername(username);
          profile.setUserBio(userBio);
          profile.setBirthDate(birthDate);
          profile.setUserPrivacy(profilePrivacy);
          profileRepository.save(profile);
          if(flag == 1){
              TokenResponse response = new TokenResponse(jwtUtil.generateToken(username), jwtUtil.generateRefreshToken(username));
              return new ResponseEntity<>(response, HttpStatus.OK);
          }
          return new ResponseEntity<>("Profile updated!", HttpStatus.ACCEPTED);
      }
      catch (Exception e){
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
    }


    public ResponseEntity<?> resetPassword(ResetDetails userDetails) {

        try {
            User user;
            user = userRepository.findByUsername(userDetails.getUsername());

            if (user == null) {
                return new ResponseEntity<String>("Username doesn't exist", HttpStatus.NOT_FOUND);
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean check = encoder.matches(userDetails.getOldPassword(), user.getUserPassword());

            if (!check) {
                return new ResponseEntity<String>("Old Password is incorrect!", HttpStatus.BAD_REQUEST);
            }

            String newPassword = userDetails.getNewPassword();

            userRepository.updatePassword(user.getUserId(), BCrypt.hashpw(newPassword, BCrypt.gensalt()));

            String mailContent = "<p>Password updated successfully</p>";
            mailService.sendMail(user.getUserEmail(), "Password updated", "Instagram", mailContent);
            return new ResponseEntity<String>("Password updated", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> forgotPassword(String userDetail) {
        try {

            UserCredentials user;

            if (userDetail.contains("@")) {
                user = findUserByEmail(userDetail);
            } else {
                user = profileRepository.findByUsername(userDetail).getUser();
            }

            if (user == null) {
                return new ResponseEntity<>("No account is registered by this email/username!", HttpStatus.NOT_FOUND);
            }

            otpService.clearOtp(user.getUserEmail());
            int otp = otpService.generateOtp(user.getUserEmail());
            System.out.println(otp);
            String mailContent = "Your otp is: " + otp;
            mailService.sendMail(user.getUserEmail(), "OTP Verification", "Instagram", mailContent);
            return new ResponseEntity<>(new Response(user.getUserEmail()), HttpStatus.OK);
        } catch (ExecutionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> setNewPassword(String userEmail, String password) {
        UserCredentials user = findUserByEmail(userEmail);
        if(!user.isVerified()){
            return new ResponseEntity<>("Profile not verified", HttpStatus.valueOf(300));
        }
        if (user == null) {
            return new ResponseEntity<>("No such Account found!", HttpStatus.valueOf(404));
        }
        user.setUserPassword(new BCryptPasswordEncoder().encode(password));
        userCredentialsRepo.save(user);
        Login login = new Login();
        login.newPassword(userEmail, password);
        if (profileRepository.findAllByUserUserId(user.getUserId()) == null) {
            return new ResponseEntity<>("Password changed. no profile is available", HttpStatus.valueOf(300));
        }

        return new ResponseEntity<>(profileRepository.findAllByUserUserId(user.getUserId()), HttpStatus.ACCEPTED);
    }

    public UUID convertToUUID(String userId) {
        return new UUID(
                new BigInteger(userId.substring(0, 16), 16).longValue(),
                new BigInteger(userId.substring(16), 16).longValue());
    }

    public ResponseEntity<?> follow(String token, String userId){
        try{

            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            UserProfile profileToBeFollowed = profileRepository.findByProfileId(UUID.fromString(userId));
            if(profile.getProfileId().equals(profileToBeFollowed.getProfileId())){
                return new ResponseEntity<>("can't follow itself", HttpStatus.valueOf(305));
            }
            if(findFollowing(profile, profileToBeFollowed)){
                return new ResponseEntity<>("already following that user", HttpStatus.valueOf(310));
            }
            profile.addFollowing(profileToBeFollowed);
            profile.addToFollowingNumber();
            profileToBeFollowed.addFollowers(profile);
            profileToBeFollowed.addToFollowersNumber();
            profileRepository.save(profile);
            profileRepository.save(profileToBeFollowed);

            return new ResponseEntity<>("user followed.", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> unfollow(String token, String userId){
        try{

            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            UserProfile profileToBeFollowed = profileRepository.findByProfileId(UUID.fromString(userId));
            if(profile.getProfileId().equals(profileToBeFollowed.getProfileId())){
                return new ResponseEntity<>("can't unfollow itself", HttpStatus.valueOf(305));
            }
            if(!findFollowing(profile, profileToBeFollowed)){
                return new ResponseEntity<>("not following that user", HttpStatus.valueOf(310));
            }
            profile.removeFollowing(profileToBeFollowed);
            profile.deductFollowingNumber();
            profileToBeFollowed.removeFollowers(profile);
            profileToBeFollowed.deductFollowersNumber();
            profileRepository.save(profile);
            profileRepository.save(profileToBeFollowed);

            return new ResponseEntity<>("user unfollowed.", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    private boolean findFollowing(UserProfile profile, UserProfile profileToBeFollowed){
        for(UserProfile p : profile.getFollowing()){
            if(p.getProfileId().equals(profileToBeFollowed.getProfileId())){
                return true;
            }
        }
        return false;
    }

    public ResponseEntity<?> searchUser(String keyword){
        try{
            List<UserShortDetailsDTO> response = new ArrayList<>();
            List<UserProfile> searchedProfilesByKeyword = profileRepository.findAll(keyword);
            if (searchedProfilesByKeyword.isEmpty()) {
                return new ResponseEntity<>("no user found!", HttpStatus.EXPECTATION_FAILED);
            }
            for (UserProfile profile : searchedProfilesByKeyword) {
                response.add(getUserShortDetails(profile));
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public UserShortDetailsDTO getUserShortDetails(UserProfile userProfile){
        return new UserShortDetailsDTO(userProfile.getProfileId(), userProfile.getProfilePicPath(), userProfile.getUsername(), userProfile.getFullName());
    }

    public ResponseEntity<?> getSuggestedUsers(String token){
        try {
            UserProfile profile = profileRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            List<UserShortDetailsDTO> response = new ArrayList<>();
            int count = 0;

            if(profile.getFollowingNumber() == 0){
                for(UserProfile profile1 : profileRepository.findByOrderByFollowingNumberDesc()){
                    response.add(getUserShortDetails(profile1));
                    if(count == 10){
                        break;
                    }
                    count++;
                }
            }
            else{
                for (UserProfile profile1 : profile.getFollowing()) {
                    response.add(getUserShortDetails(profile1));
                    if(count == 10){
                        break;
                    }
                    count++;
                }

            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

}
