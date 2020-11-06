package com.instagram.models;

import javax.persistence.*;
import java.util.UUID;


@Entity
public class Media {

    @Id
    @Column(nullable = false, unique = true)
    private UUID mediaId;
    private String mediaPath;
    private boolean pinned;

    public Media() {
        this.mediaId = UUID.randomUUID();
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
}
