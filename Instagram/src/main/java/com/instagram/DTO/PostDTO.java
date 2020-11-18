package com.instagram.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.instagram.models.Media;
import com.instagram.models.Posts;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PostDTO {

    private Posts post;
    private UserShortDetailsDTO userShortDetailsDTO;

    public PostDTO(Posts post, UserShortDetailsDTO userShortDetailsDTO) {
        this.post = post;
        this.userShortDetailsDTO = userShortDetailsDTO;
    }
}
