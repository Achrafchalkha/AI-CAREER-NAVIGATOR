package com.example.aicareernavigator.controller;

import com.example.aicareernavigator.dto.CareerRecommendationRequest;
import com.example.aicareernavigator.dto.CareerRecommendationResponse;
import com.example.aicareernavigator.service.AIRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/career")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RequiredArgsConstructor
public class CareerRecommendationController {

    private final AIRecommendationService aiRecommendationService;

    @PostMapping("/recommendations")
    public ResponseEntity<CareerRecommendationResponse> getRecommendations(
            @RequestBody CareerRecommendationRequest request) {
        try {
            CareerRecommendationResponse response = aiRecommendationService.getCareerRecommendations(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get career recommendations: " + e.getMessage(), e);
        }
    }
} 