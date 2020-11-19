package com.instagram.models;

import org.hibernate.annotations.CollectionId;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class LikeModel {

    @Id
    @Column(unique = true, nullable = false)
    private UUID id;
    private UUID profileId;
    private UUID contentId;
    private boolean likeStatus;

    public LikeModel() {
    }

    public LikeModel(UUID profileId, UUID contentId, boolean likeStatus) {
        this.id = UUID.randomUUID();
        this.profileId = profileId;
        this.contentId = contentId;
        this.likeStatus = likeStatus;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    public UUID getContentId() {
        return contentId;
    }

    public void setContentId(UUID contentId) {
        this.contentId = contentId;
    }

    public boolean isLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }
}
