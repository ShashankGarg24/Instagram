package com.instagram.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserShortDetailsDTO {

    private UUID profileId;
    private String profilePicPath;
    private String username;
    private String fullName;

    public UserShortDetailsDTO(UUID profileId, String profilePicPath, String username, String fullName) {
        this.profileId = profileId;
        this.profilePicPath = profilePicPath;
        this.username = username;
        this.fullName = fullName;
    }
}
