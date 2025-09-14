/* ============================================ */
/* FIXED AuthController.java - REPLACE YOUR ENTIRE FILE WITH THIS */
/* ============================================ */

package com.example.chat.controller;

import com.example.chat.dto.AuthResponse;
import com.example.chat.dto.LoginRequest;
import com.example.chat.dto.RegisterRequest;
import com.example.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// REMOVE THIS LINE: import jakarta.validation.Valid;
// REMOVE @Valid annotations from method parameters

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    // REMOVED @Valid annotation
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Add manual validation since we removed @Valid
            if (registerRequest.getName() == null || registerRequest.getName().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Name is required");
                error.put("field", "name");
                return ResponseEntity.badRequest().body(error);
            }

            if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Username is required");
                error.put("field", "username");
                return ResponseEntity.badRequest().body(error);
            }

            if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Email is required");
                error.put("field", "email");
                return ResponseEntity.badRequest().body(error);
            }

            if (registerRequest.getPassword() == null || registerRequest.getPassword().length() < 6) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Password must be at least 6 characters");
                error.put("field", "password");
                return ResponseEntity.badRequest().body(error);
            }

            AuthResponse response = userService.registerUser(registerRequest);

            if (response.getToken() != null || response.getMessage().contains("successfully")) {
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", response.getMessage());

                // Determine which field caused the error
                if (response.getMessage().contains("Username")) {
                    errorResponse.put("field", "username");
                } else if (response.getMessage().contains("Email")) {
                    errorResponse.put("field", "email");
                }

                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/login")
    // REMOVED @Valid annotation
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Add manual validation since we removed @Valid
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Email or username is required");
                return ResponseEntity.badRequest().body(error);
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Password is required");
                return ResponseEntity.badRequest().body(error);
            }

            AuthResponse response = userService.loginUser(loginRequest);

            if (response.getToken() != null) {
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", response.getMessage());
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Invalid authorization header");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            String token = authHeader.substring(7);
            AuthResponse response = userService.verifyToken(token);

            if (response.getToken() != null) {
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", response.getMessage());
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Token verification failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}