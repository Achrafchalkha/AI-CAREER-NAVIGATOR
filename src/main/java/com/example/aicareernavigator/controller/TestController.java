package com.example.aicareernavigator.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

@RestController
@CrossOrigin(origins = "*")
public class TestController {
    
    @Value("${openrouter.api.key}")
    private String apiKey;
    
    /**
     * Basic health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "UP");
        result.put("message", "Application is running");
        result.put("apiKeyPrefix", apiKey.substring(0, 10) + "...");
        return ResponseEntity.ok(result);
    }

    /**
     * Lightweight ping endpoint for UptimeRobot monitoring
     * This endpoint is designed to be as fast as possible to keep the app alive
     */
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
    
    /**
     * Test OpenRouter API directly
     */
    @PostMapping("/openrouter-test")
    public Mono<ResponseEntity<Map<String, Object>>> testOpenRouter(@RequestBody Map<String, String> request) {
        String prompt = request.getOrDefault("prompt", "Say hello in one sentence");
        
        // Create WebClient for OpenRouter
        WebClient webClient = WebClient.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("HTTP-Referer", "http://localhost:8036")
                .defaultHeader("X-Title", "AI Career Navigator")
                .build();
        
        // Create request payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "openai/gpt-3.5-turbo");
        payload.put("max_tokens", 100);
        payload.put("temperature", 0.7);
        
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        payload.put("messages", Arrays.asList(message));
        
        return webClient
                .post()
                .uri("/chat/completions")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", "success");
                    result.put("prompt", prompt);
                    
                    try {
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                        if (choices != null && !choices.isEmpty()) {
                            Map<String, Object> firstChoice = choices.get(0);
                            Map<String, Object> messageObj = (Map<String, Object>) firstChoice.get("message");
                            String content = (String) messageObj.get("content");
                            result.put("response", content);
                        } else {
                            result.put("response", "No response generated");
                        }
                        
                        result.put("model", response.get("model"));
                        result.put("usage", response.get("usage"));
                        result.put("raw_response", response);
                    } catch (Exception e) {
                        result.put("response", "Error parsing response: " + e.getMessage());
                        result.put("raw_response", response);
                    }
                    
                    return ResponseEntity.ok(result);
                })
                .onErrorReturn(ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Failed to call OpenRouter API",
                    "prompt", prompt
                )));
    }
    
    /**
     * Simple GET test for OpenRouter
     */
    @GetMapping("/openrouter-simple")
    public Mono<ResponseEntity<Map<String, Object>>> simpleOpenRouterTest() {
        // Create WebClient for OpenRouter
        WebClient webClient = WebClient.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
        
        // Create simple request
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "openai/gpt-3.5-turbo");
        payload.put("max_tokens", 50);
        
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "Say 'Hello from OpenRouter!' in one sentence.");
        payload.put("messages", Arrays.asList(message));
        
        return webClient
                .post()
                .uri("/chat/completions")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", "success");
                    result.put("message", "OpenRouter API is working!");
                    result.put("response", response);
                    return ResponseEntity.ok(result);
                })
                .onErrorReturn(ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "OpenRouter API failed"
                )));
    }
}
