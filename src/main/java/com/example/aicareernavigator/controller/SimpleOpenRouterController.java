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
@RequestMapping("/simple")
@CrossOrigin(origins = "*")
public class SimpleOpenRouterController {
    
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
    
    /**
     * Simple test endpoint
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "OpenRouter integration is working!");
        result.put("apiKey", apiKey.substring(0, 10) + "...");
        return ResponseEntity.ok(result);
    }
    
    /**
     * Generate text using OpenRouter
     */
    @PostMapping("/generate")
    public Mono<ResponseEntity<Map<String, Object>>> generate(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        if (prompt == null || prompt.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Prompt is required");
            return Mono.just(ResponseEntity.badRequest().body(error));
        }
        
        // Create OpenRouter request
        Map<String, Object> openRouterRequest = new HashMap<>();
        openRouterRequest.put("model", "openai/gpt-3.5-turbo");
        openRouterRequest.put("max_tokens", 2000);
        openRouterRequest.put("temperature", 0.7);
        
        // Create messages array
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        openRouterRequest.put("messages", Arrays.asList(message));
        
        return getWebClient()
                .post()
                .bodyValue(openRouterRequest)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", "success");
                    result.put("prompt", prompt);
                    
                    // Extract response content
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
                    } catch (Exception e) {
                        result.put("response", "Error parsing response: " + e.getMessage());
                        result.put("raw_response", response);
                    }
                    
                    return ResponseEntity.ok(result);
                })
                .onErrorReturn(ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Failed to generate response"
                )));
    }
    
    /**
     * Test connection to OpenRouter
     */
    @GetMapping("/connection-test")
    public Mono<ResponseEntity<Map<String, Object>>> testConnection() {
        Map<String, Object> testRequest = new HashMap<>();
        testRequest.put("model", "openai/gpt-3.5-turbo");
        testRequest.put("max_tokens", 50);
        testRequest.put("temperature", 0.7);
        
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "Say 'Hello from OpenRouter!' in one sentence.");
        testRequest.put("messages", Arrays.asList(message));
        
        return getWebClient()
                .post()
                .bodyValue(testRequest)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", "success");
                    result.put("message", "OpenRouter API connection successful!");
                    
                    try {
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                        if (choices != null && !choices.isEmpty()) {
                            Map<String, Object> firstChoice = choices.get(0);
                            Map<String, Object> messageObj = (Map<String, Object>) firstChoice.get("message");
                            String content = (String) messageObj.get("content");
                            result.put("response", content);
                        }
                    } catch (Exception e) {
                        result.put("response", "Connection successful but error parsing response");
                    }
                    
                    return ResponseEntity.ok(result);
                })
                .onErrorReturn(ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "OpenRouter API connection failed - Check your API key and network"
                )));
    }
}
