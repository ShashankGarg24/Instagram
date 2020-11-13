package com.instagram.models;

import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class CustomCategory {

    @Id
    @Column(nullable = false, unique = true)
    private UUID categoryId;
    private String profilePicPath;
    private String categoryName;
    private boolean primaryType;
    private int savedPostsNumber = 0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Media> savedPosts = new ArrayList<>();

    public CustomCategory() {
    }

    public CustomCategory(boolean primaryType, String profilePicPath, String categoryName) {
        this.categoryId = UUID.randomUUID();
        this.primaryType = primaryType;
        this.profilePicPath = profilePicPath;
        this.categoryName = categoryName;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(boolean primaryType) {
        this.primaryType = primaryType;
    }

    public int getSavedPostsNumber() {
        return savedPostsNumber;
    }

    public void increaseSavedPostsNumber() {
        ++this.savedPostsNumber;
    }

    public void decreaseSavedPostsNumber() {
        --this.savedPostsNumber;
    }


  /*  public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

   */

    public List<Media> getSavedPosts() {
        return savedPosts;
    }

    public void addSavedPosts(Media savedPosts) {
        this.savedPosts.add(savedPosts);
    }

    public void removeSavedPosts(Media savedPosts) {
        this.savedPosts.remove(savedPosts);
    }
}
