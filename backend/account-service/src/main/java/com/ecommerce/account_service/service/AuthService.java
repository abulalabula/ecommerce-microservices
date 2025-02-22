package com.ecommerce.account_service.service;

import com.ecommerce.account_service.payload.security.LoginRequest;
import com.ecommerce.account_service.payload.security.SignupRequest;
import com.ecommerce.account_service.payload.security.AuthResponse;

public interface AuthService {
    void registerUser(SignupRequest signupRequest);
    AuthResponse authenticateUser(LoginRequest loginRequest);
}
