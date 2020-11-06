package com.instagram.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class UserProfile {

    @Id
    private UUID profileId;
    private String username;
    private String fullName;
    private String userBio;
    private String userPrivacy;
    private boolean enabled;
    @CreationTimestamp
    private LocalDateTime profileCreationTimeStamp;
    private String profilePicPath;
    private String birthDate;
    private int postNumber = 0;
    private int followersCount = 0;
    private int followingCount = 0;

    @OneToMany
    private List<Media> postMedia;

    @OneToMany(mappedBy = "postByUser")
    private List<Posts> posts;

    @OneToMany(mappedBy = "commentByUser")
    private List<Comment> comments;

    @OneToMany(mappedBy = "subCommentByUser")
    private List<SubComment> subComments;

    //followers
    //followings

    public UserProfile() {
        this.profileCreationTimeStamp= LocalDateTime.now();
    }

    public UserProfile(String username, String fullName, String userPrivacy, boolean enabled) {
        this.profileId = UUID.randomUUID();
        this.username = username;
        this.fullName = fullName;
        this.userPrivacy = userPrivacy;
        this.enabled = enabled;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    public int getPostNumber() {
        return postNumber;
    }

    public void setPostNumber() {
        ++this.postNumber;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount() {
        ++this.followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount() {
        ++this.followingCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUserPrivacy() {
        return userPrivacy;
    }

    public void setUserPrivacy(String userPrivacy) {
        this.userPrivacy = userPrivacy;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getProfileCreationTimeStamp() {
        return profileCreationTimeStamp;
    }

    public void setProfileCreationTimeStamp(LocalDateTime profileCreationTimeStamp) {
        this.profileCreationTimeStamp = profileCreationTimeStamp;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    private static String parseDate(String date) {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            return new SimpleDateFormat("yyyy-MM-dd").format(d).substring(0, 10);
        } catch (ParseException e) {
            return null;
        }
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = parseDate(birthDate);
    }

    public List<Posts> getPosts() {
        return posts;
    }

    public void addPost(Posts post) {
        this.posts.add(post);
    }

    public void removePost(Posts post) {
        this.posts.remove(post);
    }

}
