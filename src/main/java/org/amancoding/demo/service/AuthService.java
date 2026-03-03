package org.amancoding.demo.service;

// CORRECT IMPORTS (pointing to dto package)
import org.amancoding.demo.dto.LoginRequest;
import org.amancoding.demo.dto.LoginResponse;
import org.amancoding.demo.dto.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}