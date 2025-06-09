package com.example.aicareernavigator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.example.aicareernavigator.enums.ExperienceLevel;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerRecommendationRequest {
    private ExperienceLevel experienceLevel;
    private List<String> interests;
    private String educationBackground;
    private Integer age;
    private String desiredField;
    private List<String> softSkills;
} 