package com.example.chat.service;

import com.example.chat.dto.AuthResponse;
import com.example.chat.dto.LoginRequest;
import com.example.chat.dto.RegisterRequest;
import com.example.chat.model.User;
import com.example.chat.repository.UserRepository;
import com.example.chat.util.SimpleAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SimpleAuthUtil authUtil;

    public AuthResponse registerUser(RegisterRequest registerRequest) {
        try {
            // Check if username exists
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return new AuthResponse("Username already exists");
            }

            // Check if email exists
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return new AuthResponse("Email already exists");
            }

            // Create new user
            User user = new User(
                    registerRequest.getName(),
                    registerRequest.getUsername(),
                    registerRequest.getEmail(),
                    passwordEncoder.encode(registerRequest.getPassword())
            );

            userRepository.save(user);
            return new AuthResponse("User registered successfully");

        } catch (Exception e) {
            return new AuthResponse("Registration failed: " + e.getMessage());
        }
    }

    public AuthResponse loginUser(LoginRequest loginRequest) {
        try {
            // Find user by username or email
            Optional<User> userOptional = userRepository.findByUsernameOrEmail(
                    loginRequest.getEmail(), loginRequest.getEmail()
            );

            if (!userOptional.isPresent()) {
                return new AuthResponse("User not found");
            }

            User user = userOptional.get();

            // Check if user is active
            if (!user.getIsActive()) {
                return new AuthResponse("Account is deactivated");
            }

            // Verify password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return new AuthResponse("Invalid password");
            }

            // Create session instead of JWT
            String sessionId = authUtil.createSession(user.getUsername(), user.getEmail(), user.getFullName());

            return new AuthResponse(sessionId, user.getUsername(), user.getFullName(), user.getEmail());

        } catch (Exception e) {
            return new AuthResponse("Login failed: " + e.getMessage());
        }
    }

    public AuthResponse verifyToken(String sessionId) {
        try {
            if (!authUtil.isValidSession(sessionId)) {
                return new AuthResponse("Invalid or expired session");
            }

            SimpleAuthUtil.UserSession session = authUtil.getSession(sessionId);
            if (session == null) {
                return new AuthResponse("Session not found");
            }

            return new AuthResponse(sessionId, session.getUsername(), session.getFullName(), session.getEmail());

        } catch (Exception e) {
            return new AuthResponse("Session verification failed: " + e.getMessage());
        }
    }
}