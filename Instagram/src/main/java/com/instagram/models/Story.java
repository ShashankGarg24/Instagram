package com.instagram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Story {

    @Id
    private UUID storyId;
    @CreationTimestamp
    private LocalDateTime storyCreationTimeStamp;
    private String  storyMediaPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private UserProfile profile;

    public Story() {
    }

    public Story(UserProfile profile) {
        this.storyId = UUID.randomUUID();
        this.storyCreationTimeStamp = LocalDateTime.now();
        this.profile = profile;
    }

    public UUID getStoryId() {
        return storyId;
    }

    public void setStoryId(UUID storyId) {
        this.storyId = storyId;
    }

    public LocalDateTime getStoryCreationTimeStamp() {
        return storyCreationTimeStamp;
    }

    public void setStoryCreationTimeStamp(LocalDateTime storyCreationTimeStamp) {
        this.storyCreationTimeStamp = storyCreationTimeStamp;
    }

    public String getStoryMediaPath() {
        return storyMediaPath;
    }

    public void setStoryMediaPath(String storyMediaPath) {
        this.storyMediaPath = storyMediaPath;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}
