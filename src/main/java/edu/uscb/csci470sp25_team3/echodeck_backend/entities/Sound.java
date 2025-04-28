// This entity represents a sound in the soundboard library

package edu.uscb.csci470sp25_team3.echodeck_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sounds")
public class Sound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name; // Name of the sound
    private String fileUrl; // URL of the sound file
    private String artist; // Name of the artist
    private String credit; // Credit or source info
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    
	public String getArtist() {	return artist; }
	public void setArtist(String artist) { this.artist = artist; }
	
	public String getCredit() {	return credit; }
	public void setCredit(String credit) { this.credit = credit; }
}






