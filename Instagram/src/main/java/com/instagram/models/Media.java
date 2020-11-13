package com.instagram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
public class Media {

    @Id
    @Column(nullable = false, unique = true)
    private UUID mediaId;
    @ElementCollection
    private List<String> mediaPath = new ArrayList<>();
    private boolean pinned;
    private UUID postId;



    public Media() {
    }

    public Media(boolean pinned, UUID postId) {
        this.mediaId = UUID.randomUUID();
        this.pinned = pinned;
        this.postId = postId;
    }

    public UUID getMediaId() {
        return mediaId;
    }

    public void setMediaId(UUID mediaId) {
        this.mediaId = mediaId;
    }

    public List<String> getMediaPath() {
        return mediaPath;
    }

    public void setmediaPath(List<String> mediaPath) {
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

}
