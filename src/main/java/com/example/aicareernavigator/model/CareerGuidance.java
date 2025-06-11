package com.example.aicareernavigator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "career_guidance")
public class CareerGuidance {
    
    @Id
    private String id;
    
    private String userId;
    private String userEmail;
    
    // User Profile Information
    private Map<String, Object> userProfile;
    
    // AI Generated Results
    private List<String> careerSuggestions;
    private List<String> roadmap;
    private List<CourseRecommendation> topCourses;
    private List<String> projectIdeas;
    private List<String> skillsToTrack;
    private JobMarketInfo jobMarket;
    
    // Raw AI Response (backup)
    private String rawAiResponse;
    
    // Metadata
    private Date createdAt;
    private Date updatedAt;
    private String aiModel;
    private String status; // "success", "error", "processing"
    
    // Nested classes for structured data
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseRecommendation {
        private String title;
        private String provider;
        private String url;
        private String description;
        private String level; // beginner, intermediate, advanced
        private Integer estimatedHours;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobMarketInfo {
        private String averageSalaryUsd;
        private String jobDemandLevel;
        private List<String> topCountriesHiring;
        private String growthProjection;
        private List<String> topCompanies;
    }
    
    // Helper methods
    public void setCreatedAt() {
        this.createdAt = new Date();
    }
    
    public void setUpdatedAt() {
        this.updatedAt = new Date();
    }
    
    // Constructor for easy creation
    public CareerGuidance(String userId, String userEmail, Map<String, Object> userProfile) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userProfile = userProfile;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.status = "processing";
    }
}
