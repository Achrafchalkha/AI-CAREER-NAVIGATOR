package com.example.aicareernavigator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private String result;
    private Usage usage;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Usage {
        private int input_tokens;
        private int output_tokens;
        private int total_tokens;
    }
} 