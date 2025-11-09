package com.gkrs.fintrek.fintrek_app.dto;

import lombok.Getter;

@Getter
public class AuthResponse {
    private String token;
    public AuthResponse(String token) {
        this.token = token;
    }
}
