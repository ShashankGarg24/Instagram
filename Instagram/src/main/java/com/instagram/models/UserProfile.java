package com.instagram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

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
    private int categoryNumber = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private UserCredentials user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CustomCategory> categories = new ArrayList<CustomCategory>();

    @OneToMany(fetch = FetchType.LAZY)
    private List<Media> postMedia = new ArrayList<Media>();

  /*  @OneToMany
    private List<Comment> comments;

    @OneToMany
    private List<SubComment> subComments;
*/
    //followers
    //followings

    public UserProfile() {

    }

    public UserProfile(String username, String fullName, String userPrivacy, boolean enabled) {
        this.profileId = UUID.randomUUID();
        this.username = username;
        this.fullName = fullName;
        this.profileCreationTimeStamp= LocalDateTime.now();
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

    public void increasePostNumber() {
        ++this.postNumber;
    }

    public void decreasePostNumber() {
        --this.postNumber;
    }

    public int getCategoryNumber() {
        return categoryNumber;
    }

    public void increaseCategoryNumber() {
        ++this.categoryNumber;
    }

    public void decreaseCategoryNumber() {
        --this.categoryNumber;
    }
/*
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
*/
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

    public UserCredentials getUser() {
        return user;
    }

    public void setUser(UserCredentials user) {
        this.user = user;
    }

    public List<CustomCategory> getCategories() {
        return categories;
    }

    public void addCategories(CustomCategory category) {
        this.categories.add(category);
    }

    public void removeCategories(CustomCategory category) {
        this.categories.remove(category);
    }


    public List<Media> getPostMedia() {
        return postMedia;
    }

    public void addPostMedia(Media postMedia) {
        this.postMedia.add(postMedia);
    }

    public void removePostMedia(Media postMedia){
        this.postMedia.remove(postMedia);
    }
}
