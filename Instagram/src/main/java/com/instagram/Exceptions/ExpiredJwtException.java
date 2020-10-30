package com.instagram.Exceptions;

import io.jsonwebtoken.Claims;

public class ExpiredJwtException extends Exception{
    private Claims claims;
    private String message;

    public ExpiredJwtException(Claims claims, String message){
       this.claims = claims;
       this.message = message;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
