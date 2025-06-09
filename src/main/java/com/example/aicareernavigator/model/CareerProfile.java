package com.example.aicareernavigator.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.example.aicareernavigator.enums.ExperienceLevel;

import java.util.List;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerProfile {
    private ExperienceLevel experienceLevel;
    private List<String> interests = new ArrayList<>();
    private String educationBackground;
    private Integer age;
    private List<String> desiredRoles = new ArrayList<>();
    private List<String> softSkills = new ArrayList<>();
    private List<String> technicalSkills = new ArrayList<>();
    private List<String> certifications = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private RoadmapProgress roadmapProgress;
} 