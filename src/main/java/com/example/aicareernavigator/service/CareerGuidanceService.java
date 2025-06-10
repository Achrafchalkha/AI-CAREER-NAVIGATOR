package com.example.aicareernavigator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

@Service
@Slf4j
public class CareerGuidanceService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.api.url}")
    private String apiUrl;

    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("HTTP-Referer", "http://localhost:8036")
                .defaultHeader("X-Title", "AI Career Navigator")
                .build();
    }

    public String generateCareerGuidance(Map<String, Object> userProfile) {
        String prompt = buildCareerGuidancePrompt(userProfile);
        
        try {
            log.info("Generating career guidance for user profile");
            
            Map<String, Object> request = createOpenRouterRequest(prompt);
            
            Map<String, Object> response = getWebClient()
                    .post()
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                    String content = (String) message.get("content");
                    log.info("Received career guidance response: {} characters", content.length());
                    return content;
                }
            }
            
            throw new RuntimeException("Failed to get valid response from OpenRouter API");
        } catch (Exception e) {
            log.error("OpenRouter API error: {}", e.getMessage(), e);
            throw new RuntimeException("OpenRouter API error: " + e.getMessage(), e);
        }
    }

    private String buildCareerGuidancePrompt(Map<String, Object> userProfile) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("You are an AI career and skill path assistant. Based on a user's profile, generate the following:\n\n");
        prompt.append("1. Career path suggestions\n");
        prompt.append("2. A step-by-step learning roadmap\n");
        prompt.append("3. Top online courses (include title, provider, and URL)\n");
        prompt.append("4. Project ideas that match the user's interest and level\n");
        prompt.append("5. Skills to track and develop\n");
        prompt.append("6. Job market insights (average salary, demand level, and top hiring countries)\n\n");
        
        prompt.append("Return the result *strictly in JSON format* using this structure:\n\n");
        prompt.append("{\n");
        prompt.append("  \"career_suggestions\": [ \"...\" ],\n");
        prompt.append("  \"roadmap\": [ \"...\" ],\n");
        prompt.append("  \"top_courses\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"title\": \"...\",\n");
        prompt.append("      \"provider\": \"...\",\n");
        prompt.append("      \"url\": \"...\"\n");
        prompt.append("    }\n");
        prompt.append("  ],\n");
        prompt.append("  \"project_ideas\": [ \"...\" ],\n");
        prompt.append("  \"skills_to_track\": [ \"...\" ],\n");
        prompt.append("  \"job_market\": {\n");
        prompt.append("    \"average_salary_usd\": \"...\",\n");
        prompt.append("    \"job_demand_level\": \"...\",\n");
        prompt.append("    \"top_countries_hiring\": [ \"...\" ]\n");
        prompt.append("  }\n");
        prompt.append("}\n\n");
        
        prompt.append("User Profile:\n");
        
        if (userProfile.containsKey("experienceLevel")) {
            prompt.append("- Experience level: ").append(userProfile.get("experienceLevel")).append("\n");
        }
        if (userProfile.containsKey("interests")) {
            prompt.append("- Interests: ").append(userProfile.get("interests")).append("\n");
        }
        if (userProfile.containsKey("education")) {
            prompt.append("- Education: ").append(userProfile.get("education")).append("\n");
        }
        if (userProfile.containsKey("age")) {
            prompt.append("- Age: ").append(userProfile.get("age")).append("\n");
        }
        if (userProfile.containsKey("desiredField")) {
            prompt.append("- Desired Field: ").append(userProfile.get("desiredField")).append("\n");
        }
        if (userProfile.containsKey("softSkills")) {
            prompt.append("- Soft skills: ").append(userProfile.get("softSkills")).append("\n");
        }
        if (userProfile.containsKey("currentSkills")) {
            prompt.append("- Current skills: ").append(userProfile.get("currentSkills")).append("\n");
        }
        if (userProfile.containsKey("location")) {
            prompt.append("- Location: ").append(userProfile.get("location")).append("\n");
        }
        if (userProfile.containsKey("careerGoals")) {
            prompt.append("- Career goals: ").append(userProfile.get("careerGoals")).append("\n");
        }
        
        return prompt.toString();
    }

    private Map<String, Object> createOpenRouterRequest(String prompt) {
        Map<String, Object> request = new HashMap<>();
        request.put("model", "openai/gpt-4o-mini"); // Using GPT-4 for better JSON formatting
        request.put("max_tokens", 3000);
        request.put("temperature", 0.7);
        
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        request.put("messages", Arrays.asList(message));
        
        return request;
    }
}
