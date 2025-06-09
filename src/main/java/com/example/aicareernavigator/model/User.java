package com.example.aicareernavigator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String email;
    
    private String password; // BCrypt encoded
    private String firstName;
    private String lastName;
    
    // Your career data
    private Map<String, Object> profile = new HashMap<>();
    private List<Map<String, Object>> aiResponses = new ArrayList<>();
    private Map<String, Object> roadmapProgress = new HashMap<>();
    
    private Date createdAt = new Date();
    private Date lastLogin;
    
    // Custom constructor for registration
    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public Map<String, Object> getProfile() {
        return profile;
    }
    
    public void setProfile(Map<String, Object> profile) {
        this.profile = profile;
    }
    
    public List<Map<String, Object>> getAiResponses() {
        return aiResponses;
    }
    
    public void setAiResponses(List<Map<String, Object>> aiResponses) {
        this.aiResponses = aiResponses;
    }
    
    public Map<String, Object> getRoadmapProgress() {
        return roadmapProgress;
    }
    
    public void setRoadmapProgress(Map<String, Object> roadmapProgress) {
        this.roadmapProgress = roadmapProgress;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}