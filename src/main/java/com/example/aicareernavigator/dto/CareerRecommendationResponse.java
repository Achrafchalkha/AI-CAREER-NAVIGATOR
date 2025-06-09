package com.example.aicareernavigator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerRecommendationResponse {
    private List<String> careerSuggestions;
    private List<String> roadmap;
    private List<Course> topCourses;
    private List<String> projectIdeas;
    private List<String> skillsToTrack;
    private JobMarket jobMarket;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Course {
        private String title;
        private String provider;
        private String url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobMarket {
        private String averageSalaryUsd;
        private String jobDemandLevel;
        private List<String> topCountriesHiring;
    }
} 