package com.example.aicareernavigator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;

@Configuration
public class AIConfig {

    @Value("${openrouter.api.key}")
    private String openRouterApiKey;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getOpenRouterApiKey() {
        return openRouterApiKey;
    }
}