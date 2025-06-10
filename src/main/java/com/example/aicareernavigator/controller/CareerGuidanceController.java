package com.example.aicareernavigator.controller;

import com.example.aicareernavigator.service.CareerGuidanceService;
import com.example.aicareernavigator.service.AuthService;
import com.example.aicareernavigator.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.HashMap;

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

            // Generate career guidance
            String guidanceResponse = careerGuidanceService.generateCareerGuidance(userProfile);
            
            // Try to parse the response as JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> result = new HashMap<>();
            
            try {
                // Attempt to parse the AI response as JSON
                Object parsedResponse = objectMapper.readValue(guidanceResponse, Object.class);
                result.put("status", "success");
                result.put("user_id", user.getId());
                result.put("guidance", parsedResponse);
            } catch (Exception parseException) {
                // If parsing fails, return the raw response
                result.put("status", "success");
                result.put("user_id", user.getId());
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
}
