package com.instagram.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class PostMedia {

    @Id
    @Column(nullable = false, unique = true)
    private UUID mediaId;
    @ManyToOne
    private Posts _post;

    public PostMedia() {
        this.mediaId = UUID.randomUUID();
    }

    public UUID getMediaId() {
        return mediaId;
    }

    public void setMediaId(UUID mediaId) {
        this.mediaId = mediaId;
    }

    public Posts get_post() {
        return _post;
    }

    public void set_post(Posts _post) {
        this._post = _post;
    }
}
