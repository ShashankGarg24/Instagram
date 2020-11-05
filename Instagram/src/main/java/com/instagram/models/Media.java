package com.instagram.models;

import javax.persistence.*;
import java.util.UUID;


@Entity
public class Media {

    @Id
    @Column(nullable = false, unique = true)
    private UUID mediaId;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "profilePic")
    private User user;
    @ManyToOne
    private Posts mediaPost;

    public Media() {
        this.mediaId = UUID.randomUUID();
    }

    public UUID getMediaId() {
        return mediaId;
    }

    public void setMediaId(UUID mediaId) {
        this.mediaId = mediaId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Posts getMediaPost() {
        return mediaPost;
    }

    public void setMediaPost(Posts mediaPost) {
        this.mediaPost = mediaPost;
    }
}
