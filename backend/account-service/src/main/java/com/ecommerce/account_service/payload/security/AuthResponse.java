package com.ecommerce.account_service.payload.security;

public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String role;

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
//        this.role = role;
    }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getTokenType() { return tokenType; }
}
