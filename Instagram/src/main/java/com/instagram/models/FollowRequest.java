package com.instagram.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class FollowRequest {


    @Id
    private UUID requestId;
    private UUID requestTo;
    private UUID requestFrom;

    public FollowRequest() {
    }

    public FollowRequest(UUID requestTo, UUID requestFrom) {
        this.requestId = UUID.randomUUID();
        this.requestTo = requestTo;
        this.requestFrom = requestFrom;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public UUID getRequestTo() {
        return requestTo;
    }

    public void setRequestTo(UUID requestTo) {
        this.requestTo = requestTo;
    }

    public UUID getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(UUID requestFrom) {
        this.requestFrom = requestFrom;
    }
}
