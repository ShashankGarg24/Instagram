package com.instagram.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.instagram.models.Media;
import com.instagram.models.Posts;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PostDTO {

    private Posts post;
    private Media postMedia;

    public PostDTO(Posts post, Media postMedia) {
        this.post = post;
        this.postMedia = postMedia;
    }
}
