package com.instagram.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.instagram.models.Media;
import com.instagram.models.Posts;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PostDTO {

    private Posts post;
    private List<Media> postMedia = new ArrayList<>();

    public PostDTO(Posts post, List<Media> postMedia) {
        this.post = post;
        this.postMedia = postMedia;
    }
}
