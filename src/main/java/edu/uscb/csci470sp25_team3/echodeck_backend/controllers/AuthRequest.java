// This class is used to handle incoming JSON data for user login and registration requests

package edu.uscb.csci470sp25_team3.echodeck_backend.controllers;

public class AuthRequest {
    private String email;
    private String password;
    private String role;

    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
