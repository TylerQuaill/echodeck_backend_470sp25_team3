package edu.uscb.csci470sp25_team3.echodeck_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String role;

	public User() {}

	public User(String email, String password, String role) {
	        this.email = email;
	        this.password = password;
	        this.role = role;
	    }

	// Getters
	public Long getId() {
		return id;
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

	// Setters
	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String role) {
		this.role = role;
	}

	// For debugging purposes
	@Override
	public String toString() {
		return "AppUser{" + "id=" + id + ", email='" + email + '\'' + ", role='" + role + '\'' + '}';
	}
}