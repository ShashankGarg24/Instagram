package com.instagram.models;

public class LoginResponse {

    private final String jwt;
    private User user;

    public LoginResponse(String jwt,User user){
        this.user = user;
        this.jwt = jwt;
    }

    public String getJwt(){
        return jwt;
    }

    public User getUser(){
        return user;
    }
}
