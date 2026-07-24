package com.example.AiApplication.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Map;

@Service
public class GeminiService{
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    private final WebClient webClient;
    public GeminiService(WebClient.Builder builder){
        webClient=builder.build();
    }
//send data to gemini and collect response but we need details as a prompt from activity data
    //so for prompting goes to ActivityAi
    public String getRecommendation(String details){
        Map<String,Object> requestBody=Map.of("model","gemini-3.6-flash","input",details);
        String response=webClient.post()
                .uri(geminiApiUrl)
                .header("Content-Type","application/json")
                .header("x-goog-api-key",geminiApiKey)
                .bodyValue(requestBody)
                .retrieve().bodyToMono(String.class)
                .block();
        return response;
    }
}
