package com.nit.service;

import com.nit.dto.request.LoginRequest;
import com.nit.dto.request.RegisterRequest;
import com.nit.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}