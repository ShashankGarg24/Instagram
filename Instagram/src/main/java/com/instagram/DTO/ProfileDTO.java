package com.instagram.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.instagram.models.Media;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProfileDTO {
    private UUID profileId;
    private String username;
    private String fullName;
    private String userBio;
    private String userPrivacy;
    private boolean enabled;
    private LocalDateTime profileCreationTimeStamp;
    private String profilePicPath;
    private String birthDate;
    private Set<Media> postMedia = new HashSet<>();

    public ProfileDTO(UUID profileId, String username, String fullName, String userBio, String userPrivacy, boolean enabled, LocalDateTime profileCreationTimeStamp, String profilePicPath, String birthDate, Set<Media> postMedia) {
        this.profileId = profileId;
        this.username = username;
        this.fullName = fullName;
        this.userBio = userBio;
        this.userPrivacy = userPrivacy;
        this.enabled = enabled;
        this.profileCreationTimeStamp = profileCreationTimeStamp;
        this.profilePicPath = profilePicPath;
        this.birthDate = birthDate;
        this.postMedia = postMedia;
    }
}
