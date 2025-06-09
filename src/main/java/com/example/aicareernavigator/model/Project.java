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
@Document(collection = "projects")
public class Project {
    @Id
    private String id;
    private String title;
    private String description;
    private String githubUrl;
    private String liveUrl;
    private List<String> technologies = new ArrayList<>();
    private ProjectStatus status;
    private List<String> screenshots = new ArrayList<>();
    private Date startDate;
    private Date completionDate;
    private List<Milestone> milestones = new ArrayList<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Milestone {
    private String title;
    private String description;
    private boolean completed;
    private Date completionDate;
}

enum ProjectStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    ON_HOLD
} 