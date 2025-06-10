package com.example.aicareernavigator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenRouterConfig {
    
    @Value("${openrouter.api.key}")
    private String apiKey;
    
    @Value("${openrouter.api.url}")
    private String apiUrl;
    
    @Value("${openrouter.api.models-url}")
    private String modelsUrl;
    
    @Value("${openrouter.site.url}")
    private String siteUrl;
    
    @Value("${openrouter.site.name}")
    private String siteName;
    
    @Bean
    public WebClient openRouterWebClient() {
        return WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("HTTP-Referer", siteUrl)
                .defaultHeader("X-Title", siteName)
                .build();
    }
    
    @Bean
    public WebClient openRouterModelsWebClient() {
        return WebClient.builder()
                .baseUrl(modelsUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getApiUrl() {
        return apiUrl;
    }
    
    public String getModelsUrl() {
        return modelsUrl;
    }
    
    public String getSiteUrl() {
        return siteUrl;
    }
    
    public String getSiteName() {
        return siteName;
    }
}
