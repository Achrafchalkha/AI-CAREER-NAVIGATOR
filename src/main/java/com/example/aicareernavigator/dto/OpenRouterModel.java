package com.example.aicareernavigator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenRouterModel {
    private String id;
    private String name;
    private Long created;
    private String description;
    private Architecture architecture;
    private TopProvider top_provider;
    private Pricing pricing;
    private Integer context_length;
    private String hugging_face_id;
    private Map<String, Object> per_request_limits;
    private List<String> supported_parameters;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Architecture {
        private List<String> input_modalities;
        private List<String> output_modalities;
        private String tokenizer;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopProvider {
        private Boolean is_moderated;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pricing {
        private String prompt;
        private String completion;
        private String image;
        private String request;
        private String input_cache_read;
        private String input_cache_write;
        private String web_search;
        private String internal_reasoning;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class OpenRouterModelsResponse {
    private List<OpenRouterModel> data;
}
