package com.instagram.models;

import com.instagram.DTO.ProfileDTO;

public class LoginResponse {

    private final String accessToken;
    private final String refreshToken;
    private UserProfile user;


    public LoginResponse(String accessToken, String refreshToken, UserProfile user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public UserProfile getUser(){
        return user;
    }
}
