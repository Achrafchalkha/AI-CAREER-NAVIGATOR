package com.example.aicareernavigator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;

@Configuration
public class AIConfig {
    
    @Value("${ai.openai.api-key}")
    private String openAiApiKey;
    
    @Value("${ai.google.api-key}")
    private String googleApiKey;
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
} 