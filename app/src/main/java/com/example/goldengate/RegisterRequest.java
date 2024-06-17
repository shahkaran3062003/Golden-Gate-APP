package com.example.goldengate;
public class RegisterRequest {
    private String username;
    private String email;
    private String password;

    private  String role;

    // Constructors, getters, and setters
    public RegisterRequest(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

// Getters and setters (or use Lombok annotations if Lombok is configured)



}