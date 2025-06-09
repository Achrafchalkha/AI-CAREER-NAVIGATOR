package com.example.aicareernavigator.service;

import com.example.aicareernavigator.dto.CareerRecommendationRequest;
import com.example.aicareernavigator.dto.CareerRecommendationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIRecommendationService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ai.google.api-key}")
    private String apiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";

    public CareerRecommendationResponse getCareerRecommendations(CareerRecommendationRequest request) {
        try {
            String prompt = buildPrompt(request);
            String jsonResponse = callGeminiAPI(prompt);
            return parseResponse(jsonResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get AI recommendations: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(CareerRecommendationRequest request) {
        return String.format("""
            You are an AI career and skill path assistant. Based on the following user profile, generate career recommendations:
            
            User Profile:
            - Experience level: %s
            - Interests: %s
            - Education: %s
            - Age: %d
            - Desired Field: %s
            - Soft skills: %s
            
            Return the result strictly in JSON format using this structure:
            {
              "career_suggestions": [ "..." ],
              "roadmap": [ "..." ],
              "top_courses": [
                {
                  "title": "...",
                  "provider": "...",
                  "url": "..."
                }
              ],
              "project_ideas": [ "..." ],
              "skills_to_track": [ "..." ],
              "job_market": {
                "average_salary_usd": "...",
                "job_demand_level": "...",
                "top_countries_hiring": [ "..." ]
              }
            }
            """,
            request.getExperienceLevel(),
            String.join(", ", request.getInterests()),
            request.getEducationBackground(),
            request.getAge(),
            request.getDesiredField(),
            String.join(", ", request.getSoftSkills())
        );
    }

    private String callGeminiAPI(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt)
                ))
            )
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        
        var response = restTemplate.postForObject(
            GEMINI_API_URL,
            request,
            Map.class
        );

        if (response != null && response.containsKey("candidates")) {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            if (!candidates.isEmpty()) {
                Map<String, Object> firstCandidate = candidates.get(0);
                List<Map<String, Object>> parts = (List<Map<String, Object>>) 
                    ((List<Map<String, Object>>) firstCandidate.get("content")).get(0).get("parts");
                return (String) parts.get(0).get("text");
            }
        }

        throw new RuntimeException("Failed to get valid response from Gemini API");
    }

    private CareerRecommendationResponse parseResponse(String jsonResponse) {
        try {
            // First, parse as a Map to convert from snake_case to camelCase
            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);
            
            // Then convert to our DTO
            return objectMapper.convertValue(responseMap, CareerRecommendationResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + e.getMessage(), e);
        }
    }
} 