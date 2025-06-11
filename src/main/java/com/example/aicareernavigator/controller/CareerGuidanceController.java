package com.example.aicareernavigator.controller;

import com.example.aicareernavigator.service.CareerGuidanceService;
import com.example.aicareernavigator.service.AuthService;
import com.example.aicareernavigator.model.User;
import com.example.aicareernavigator.model.CareerGuidance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/career")
@CrossOrigin(origins = "*")
public class CareerGuidanceController {

    @Autowired
    private CareerGuidanceService careerGuidanceService;

    @Autowired
    private AuthService authService;

    @PostMapping("/guidance")
    public ResponseEntity<?> getCareerGuidance(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> userProfile) {
        try {
            // Verify user authentication
            User user = authService.getUserFromToken(token);

            // Validate required fields
            if (userProfile == null || userProfile.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "User profile is required");
                return ResponseEntity.badRequest().body(error);
            }

            // Generate and save career guidance
            CareerGuidance savedGuidance = careerGuidanceService.generateAndSaveCareerGuidance(user, userProfile);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("user_id", user.getId());
            result.put("guidance_id", savedGuidance.getId());
            result.put("created_at", savedGuidance.getCreatedAt());

            // Return structured data if available, otherwise raw response
            if (savedGuidance.getCareerSuggestions() != null) {
                Map<String, Object> structuredGuidance = new HashMap<>();
                structuredGuidance.put("career_suggestions", savedGuidance.getCareerSuggestions());
                structuredGuidance.put("roadmap", savedGuidance.getRoadmap());
                structuredGuidance.put("top_courses", savedGuidance.getTopCourses());
                structuredGuidance.put("project_ideas", savedGuidance.getProjectIdeas());
                structuredGuidance.put("skills_to_track", savedGuidance.getSkillsToTrack());
                structuredGuidance.put("job_market", savedGuidance.getJobMarket());
                result.put("guidance", structuredGuidance);
            } else {
                result.put("guidance", savedGuidance.getRawAiResponse());
                result.put("note", "Response saved but not in expected JSON format");
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/guidance/public")
    public ResponseEntity<?> getCareerGuidancePublic(@RequestBody Map<String, Object> userProfile) {
        try {
            // Validate required fields
            if (userProfile == null || userProfile.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "User profile is required");
                return ResponseEntity.badRequest().body(error);
            }

            // Generate career guidance without authentication
            String guidanceResponse = careerGuidanceService.generateCareerGuidance(userProfile);
            
            // Try to parse the response as JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> result = new HashMap<>();
            
            try {
                // Attempt to parse the AI response as JSON
                Object parsedResponse = objectMapper.readValue(guidanceResponse, Object.class);
                result.put("status", "success");
                result.put("guidance", parsedResponse);
            } catch (Exception parseException) {
                // If parsing fails, return the raw response
                result.put("status", "success");
                result.put("guidance", guidanceResponse);
                result.put("note", "Response received but not in expected JSON format");
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/example")
    public ResponseEntity<Map<String, Object>> getExampleProfile() {
        Map<String, Object> example = new HashMap<>();
        example.put("experienceLevel", "Intermediate");
        example.put("interests", "Web Development");
        example.put("education", "Bachelor's in Computer Science");
        example.put("age", 23);
        example.put("desiredField", "Frontend Development");
        example.put("softSkills", "Problem-solving, adaptability, teamwork");
        example.put("currentSkills", "HTML, CSS, JavaScript, React");
        example.put("location", "United States");
        example.put("careerGoals", "Become a senior frontend developer");
        
        Map<String, Object> response = new HashMap<>();
        response.put("example_profile", example);
        response.put("description", "Use this example profile structure for the /guidance endpoint");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getCareerHistory(@RequestHeader("Authorization") String token) {
        try {
            User user = authService.getUserFromToken(token);
            List<CareerGuidance> history = careerGuidanceService.getUserCareerHistory(user.getId());

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("user_id", user.getId());
            result.put("total_sessions", history.size());
            result.put("history", history);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(401).body(error);
        }
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<?> getCareerGuidanceById(
            @RequestHeader("Authorization") String token,
            @PathVariable String id) {
        try {
            User user = authService.getUserFromToken(token);
            CareerGuidance guidance = careerGuidanceService.getCareerGuidanceById(id, user.getId());

            if (guidance == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Career guidance not found");
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("guidance", guidance);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(401).body(error);
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestCareerGuidance(@RequestHeader("Authorization") String token) {
        try {
            User user = authService.getUserFromToken(token);
            CareerGuidance latest = careerGuidanceService.getLatestCareerGuidance(user.getId());

            if (latest == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "No career guidance found for this user");
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("guidance", latest);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(401).body(error);
        }
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<?> deleteCareerGuidance(
            @RequestHeader("Authorization") String token,
            @PathVariable String id) {
        try {
            User user = authService.getUserFromToken(token);
            careerGuidanceService.deleteCareerGuidance(id, user.getId());

            Map<String, String> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Career guidance deleted successfully");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getUserStats(@RequestHeader("Authorization") String token) {
        try {
            User user = authService.getUserFromToken(token);
            long totalSessions = careerGuidanceService.getUserGuidanceCount(user.getId());
            CareerGuidance latest = careerGuidanceService.getLatestCareerGuidance(user.getId());

            Map<String, Object> stats = new HashMap<>();
            stats.put("user_id", user.getId());
            stats.put("total_guidance_sessions", totalSessions);
            stats.put("latest_session_date", latest != null ? latest.getCreatedAt() : null);
            stats.put("has_guidance", totalSessions > 0);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("stats", stats);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(401).body(error);
        }
    }

    @PostMapping("/reprocess/{id}")
    public ResponseEntity<?> reprocessCareerGuidance(
            @RequestHeader("Authorization") String token,
            @PathVariable String id) {
        try {
            User user = authService.getUserFromToken(token);
            CareerGuidance reprocessed = careerGuidanceService.reprocessCareerGuidance(id, user.getId());

            if (reprocessed == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Career guidance not found");
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Career guidance reprocessed successfully");
            result.put("guidance", reprocessed);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/reprocess-all")
    public ResponseEntity<?> reprocessAllCareerGuidance(@RequestHeader("Authorization") String token) {
        try {
            User user = authService.getUserFromToken(token);
            careerGuidanceService.reprocessAllUserCareerGuidance(user.getId());

            Map<String, String> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "All career guidance records reprocessed successfully");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
