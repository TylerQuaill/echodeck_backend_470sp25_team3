package edu.uscb.csci470sp25_team3.echodeck_backend.controllers;

public class AuthRequest {
    private String email;
    private String password;
    private String role;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
