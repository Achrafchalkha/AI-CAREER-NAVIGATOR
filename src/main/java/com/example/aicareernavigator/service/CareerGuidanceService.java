package com.example.aicareernavigator.service;

import com.example.aicareernavigator.model.CareerGuidance;
import com.example.aicareernavigator.model.User;
import com.example.aicareernavigator.repository.CareerGuidanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Date;

@Service
@Slf4j
public class CareerGuidanceService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.api.url}")
    private String apiUrl;

    @Autowired
    private CareerGuidanceRepository careerGuidanceRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("HTTP-Referer", "http://localhost:8036")
                .defaultHeader("X-Title", "AI Career Navigator")
                .build();
    }

    public CareerGuidance generateAndSaveCareerGuidance(User user, Map<String, Object> userProfile) {
        // Create initial record
        CareerGuidance careerGuidance = new CareerGuidance(user.getId(), user.getEmail(), userProfile);
        careerGuidance.setAiModel("openai/gpt-4o-mini");
        careerGuidance = careerGuidanceRepository.save(careerGuidance);

        try {
            log.info("Generating career guidance for user: {}", user.getEmail());

            String prompt = buildCareerGuidancePrompt(userProfile);
            Map<String, Object> request = createOpenRouterRequest(prompt);

            Map<String, Object> response = getWebClient()
                    .post()
                    .uri("/chat/completions")
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

                    // Save raw response
                    careerGuidance.setRawAiResponse(content);

                    // Parse and save structured data
                    parseAndSaveStructuredData(careerGuidance, content);

                    careerGuidance.setStatus("success");
                    careerGuidance.setUpdatedAt();

                    log.info("Successfully generated and saved career guidance for user: {}", user.getEmail());
                    return careerGuidanceRepository.save(careerGuidance);
                }
            }

            careerGuidance.setStatus("error");
            careerGuidance.setRawAiResponse("Failed to get valid response from OpenRouter API");
            careerGuidance.setUpdatedAt();
            careerGuidanceRepository.save(careerGuidance);

            throw new RuntimeException("Failed to get valid response from OpenRouter API");
        } catch (Exception e) {
            log.error("OpenRouter API error for user {}: {}", user.getEmail(), e.getMessage(), e);

            careerGuidance.setStatus("error");
            careerGuidance.setRawAiResponse("Error: " + e.getMessage());
            careerGuidance.setUpdatedAt();
            careerGuidanceRepository.save(careerGuidance);

            throw new RuntimeException("OpenRouter API error: " + e.getMessage(), e);
        }
    }

    public String generateCareerGuidance(Map<String, Object> userProfile) {
        // Legacy method for public access (without saving)
        String prompt = buildCareerGuidancePrompt(userProfile);

        try {
            log.info("Generating career guidance for anonymous user");

            Map<String, Object> request = createOpenRouterRequest(prompt);

            Map<String, Object> response = getWebClient()
                    .post()
                    .uri("/chat/completions")
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

    private void parseAndSaveStructuredData(CareerGuidance careerGuidance, String aiResponse) {
        try {
            // Clean the AI response - remove markdown code blocks if present
            String cleanedResponse = cleanJsonResponse(aiResponse);

            // Try to parse the cleaned AI response as JSON
            Map<String, Object> parsedResponse = objectMapper.readValue(cleanedResponse, new TypeReference<Map<String, Object>>() {});

            // Extract career suggestions
            if (parsedResponse.containsKey("career_suggestions")) {
                List<String> careerSuggestions = (List<String>) parsedResponse.get("career_suggestions");
                careerGuidance.setCareerSuggestions(careerSuggestions);
            }

            // Extract roadmap
            if (parsedResponse.containsKey("roadmap")) {
                List<String> roadmap = (List<String>) parsedResponse.get("roadmap");
                careerGuidance.setRoadmap(roadmap);
            }

            // Extract and parse courses
            if (parsedResponse.containsKey("top_courses")) {
                List<Map<String, Object>> coursesData = (List<Map<String, Object>>) parsedResponse.get("top_courses");
                List<CareerGuidance.CourseRecommendation> courses = coursesData.stream()
                    .map(courseData -> {
                        CareerGuidance.CourseRecommendation course = new CareerGuidance.CourseRecommendation();
                        course.setTitle((String) courseData.get("title"));
                        course.setProvider((String) courseData.get("provider"));
                        course.setUrl((String) courseData.get("url"));
                        course.setDescription((String) courseData.get("description"));
                        course.setLevel((String) courseData.get("level"));
                        if (courseData.containsKey("estimatedHours")) {
                            course.setEstimatedHours((Integer) courseData.get("estimatedHours"));
                        }
                        return course;
                    })
                    .toList();
                careerGuidance.setTopCourses(courses);
            }

            // Extract project ideas
            if (parsedResponse.containsKey("project_ideas")) {
                List<String> projectIdeas = (List<String>) parsedResponse.get("project_ideas");
                careerGuidance.setProjectIdeas(projectIdeas);
            }

            // Extract skills to track
            if (parsedResponse.containsKey("skills_to_track")) {
                List<String> skillsToTrack = (List<String>) parsedResponse.get("skills_to_track");
                careerGuidance.setSkillsToTrack(skillsToTrack);
            }

            // Extract and parse job market info
            if (parsedResponse.containsKey("job_market")) {
                Map<String, Object> jobMarketData = (Map<String, Object>) parsedResponse.get("job_market");
                CareerGuidance.JobMarketInfo jobMarket = new CareerGuidance.JobMarketInfo();
                jobMarket.setAverageSalaryUsd((String) jobMarketData.get("average_salary_usd"));
                jobMarket.setJobDemandLevel((String) jobMarketData.get("job_demand_level"));
                jobMarket.setTopCountriesHiring((List<String>) jobMarketData.get("top_countries_hiring"));
                jobMarket.setGrowthProjection((String) jobMarketData.get("growth_projection"));
                jobMarket.setTopCompanies((List<String>) jobMarketData.get("top_companies"));
                careerGuidance.setJobMarket(jobMarket);
            }

            log.info("Successfully parsed structured data from AI response");
        } catch (Exception e) {
            log.warn("Failed to parse AI response as structured JSON, saving as raw text: {}", e.getMessage());
            // If parsing fails, the raw response is already saved
        }
    }

    private String cleanJsonResponse(String aiResponse) {
        if (aiResponse == null || aiResponse.trim().isEmpty()) {
            return aiResponse;
        }

        String cleaned = aiResponse.trim();

        // Remove markdown code blocks
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7); // Remove "```json"
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3); // Remove "```"
        }

        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3); // Remove trailing "```"
        }

        // Remove any leading/trailing whitespace
        cleaned = cleaned.trim();

        log.debug("Cleaned JSON response: {}", cleaned.substring(0, Math.min(cleaned.length(), 200)) + "...");

        return cleaned;
    }

    // Methods for retrieving saved career guidance
    public List<CareerGuidance> getUserCareerHistory(String userId) {
        return careerGuidanceRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public CareerGuidance getLatestCareerGuidance(String userId) {
        return careerGuidanceRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
                .orElse(null);
    }

    public CareerGuidance getCareerGuidanceById(String id, String userId) {
        CareerGuidance guidance = careerGuidanceRepository.findById(id).orElse(null);
        if (guidance != null && !guidance.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied: Career guidance belongs to another user");
        }
        return guidance;
    }

    public List<CareerGuidance> getCareerGuidanceByField(String userId, String desiredField) {
        return careerGuidanceRepository.findByUserIdAndDesiredFieldContaining(userId, desiredField);
    }

    public long getUserGuidanceCount(String userId) {
        return careerGuidanceRepository.countByUserId(userId);
    }

    public void deleteCareerGuidance(String id, String userId) {
        CareerGuidance guidance = getCareerGuidanceById(id, userId);
        if (guidance != null) {
            careerGuidanceRepository.delete(guidance);
        }
    }

    /**
     * Reprocess existing career guidance records that have null structured data
     * This method can be called to fix records that were saved before the JSON parsing fix
     */
    public CareerGuidance reprocessCareerGuidance(String id, String userId) {
        CareerGuidance guidance = getCareerGuidanceById(id, userId);
        if (guidance != null && guidance.getRawAiResponse() != null && guidance.getCareerSuggestions() == null) {
            log.info("Reprocessing career guidance {} for user {}", id, userId);

            // Parse and save structured data from the raw response
            parseAndSaveStructuredData(guidance, guidance.getRawAiResponse());
            guidance.setUpdatedAt();

            return careerGuidanceRepository.save(guidance);
        }
        return guidance;
    }

    /**
     * Reprocess all career guidance records for a user that have null structured data
     */
    public void reprocessAllUserCareerGuidance(String userId) {
        List<CareerGuidance> userGuidance = getUserCareerHistory(userId);
        for (CareerGuidance guidance : userGuidance) {
            if (guidance.getRawAiResponse() != null && guidance.getCareerSuggestions() == null) {
                log.info("Reprocessing career guidance {} for user {}", guidance.getId(), userId);
                parseAndSaveStructuredData(guidance, guidance.getRawAiResponse());
                guidance.setUpdatedAt();
                careerGuidanceRepository.save(guidance);
            }
        }
    }
}
