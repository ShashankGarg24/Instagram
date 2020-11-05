package com.instagram.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
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


    @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private Media profilePic;

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

    public Media getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Media profilePic) {
        this.profilePic = profilePic;
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
