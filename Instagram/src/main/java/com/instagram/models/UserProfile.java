package com.instagram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.PersistentObjectException;
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
    @JsonIgnore
    private LocalDateTime profileCreationTimeStamp;
    private String profilePicPath;
    private String birthDate;
    private int postNumber = 0;
   // private int storyNumber = 0;
    private int categoryNumber = 0;
    private int followersNumber = 0;
    private int followingNumber = 0;


    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private UserCredentials user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CustomCategory> categories = new ArrayList<CustomCategory>();

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Media> postMedia = new ArrayList<Media>();

    @OneToMany(fetch = FetchType.LAZY)
    private List<Posts> posts = new ArrayList<Posts>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserProfile> followers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserProfile> following = new ArrayList<>();

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

    public int getFollowersNumber() {
        return followersNumber;
    }

    public void addToFollowersNumber() {
        ++this.followersNumber;
    }

    public void deductFollowersNumber() {
        --this.followersNumber;
    }


    public int getFollowingNumber() {
        return followingNumber;
    }

    public void addToFollowingNumber() {
        ++this.followingNumber;
    }

    public void deductFollowingNumber() {
        --this.followingNumber;
    }

    /*public int getStoryNumber() {
        return storyNumber;
    }

    public void increaseStoryNumber() {
        ++this.storyNumber;
    }

    public void decreaseStoryNumber() {
        --this.storyNumber;
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

    public List<Posts> getPosts() {
        return posts;
    }

    public void addToPosts(Posts post) {
        this.posts.add(post);
    }

    public void removeFromPosts(Posts post) {
        this.posts.remove(post);
    }

    public List<UserProfile> getFollowers() {
        return followers;
    }

    public void addFollowers(UserProfile follower) {
        this.followers.add(follower);
    }

    public void removeFollowers(UserProfile follower) {
        this.followers.remove(follower);
    }

    public List<UserProfile> getFollowing() {
        return following;
    }

    public void addFollowing(UserProfile following) {
        this.following.add(following);
    }

    public void removeFollowing(UserProfile following) {
        this.following.remove(following);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserProfile)) {
            return false;
        }
        UserProfile other = (UserProfile) obj;
        return this.profileId.equals(other.profileId);
    }

    @Override
    public int hashCode() {
        return profileId.hashCode();
    }
}
