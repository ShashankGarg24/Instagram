package com.instagram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.UUID;


@Entity
public class Media {

    @Id
    @Column(nullable = false, unique = true)
    private UUID mediaId;
    private String mediaPath;
    private boolean pinned;
    private UUID postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Posts posts;


    public Media() {
        this.mediaId = UUID.randomUUID();
    }

    public void setData(String mediaPath, boolean pinned, UUID postId, Posts posts) {
        this.mediaPath = mediaPath;
        this.pinned = pinned;
        this.postId = postId;
        this.posts = posts;
    }

    public UUID getMediaId() {
        return mediaId;
    }

    public void setMediaId(UUID mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }
}
