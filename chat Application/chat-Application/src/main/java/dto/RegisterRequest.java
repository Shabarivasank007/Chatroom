/* ============================================ */
/* FIXED RegisterRequest.java - REPLACE YOUR ENTIRE FILE WITH THIS */
/* ============================================ */

package com.example.chat.dto;

// REMOVE THESE LINES:
// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Size;

// REMOVE ALL @NotBlank, @Email, @Size ANNOTATIONS

public class RegisterRequest {
    private String name;
    private String username;
    private String email;
    private String password;

    public RegisterRequest() {}

    public RegisterRequest(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}