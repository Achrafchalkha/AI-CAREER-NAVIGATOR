package com.example.aicareernavigator.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "roadmap_progress")
public class RoadmapProgress {
    @Id
    private String id;
    private String userId;
    private String careerPath;
    private List<LearningModule> completedModules = new ArrayList<>();
    private List<LearningModule> inProgressModules = new ArrayList<>();
    private List<LearningModule> upcomingModules = new ArrayList<>();
    private int progressPercentage;
    private Date lastUpdated;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class LearningModule {
    private String title;
    private String description;
    private ModuleType type; // COURSE, PROJECT, ASSESSMENT
    private String resourceUrl;
    private boolean completed;
    private int progressPercentage;
    private Date startDate;
    private Date completionDate;
    private List<String> skills = new ArrayList<>();
}

enum ModuleType {
    COURSE,
    PROJECT,
    ASSESSMENT,
    PRACTICE
} 