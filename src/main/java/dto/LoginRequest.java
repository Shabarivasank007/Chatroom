/* ============================================ */
/* FIXED LoginRequest.java - REPLACE YOUR ENTIRE FILE WITH THIS */
/* ============================================ */

package com.example.chat.dto;

// REMOVE THIS LINE: import jakarta.validation.constraints.NotBlank;
// REMOVE THESE ANNOTATIONS: @NotBlank

public class LoginRequest {
    // Remove @NotBlank annotation
    private String email;

    // Remove @NotBlank annotation
    private String password;

    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}