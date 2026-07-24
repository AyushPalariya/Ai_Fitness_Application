package com.example.AiApplication.Services;

import com.example.AiApplication.Entities.Activity;
import com.example.AiApplication.Entities.Recommendation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@Data
public class ActivityAiGemini {

    private final GeminiService geminiService;

    public ActivityAiGemini(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public Recommendation generateRecommendation(Activity activity) {
        String prompt = createPrompt(activity);
        String aiResponse= geminiService.getRecommendation(prompt);
        log.info("Response from AI {}",aiResponse);
        return processAiResponse(activity,aiResponse);
    }

    private Recommendation processAiResponse(Activity activity, String aiResponse) {
        try{
            ObjectMapper mapper=new ObjectMapper();
            JsonNode rootNode=mapper.readTree(aiResponse);
            JsonNode textNode= rootNode.path("steps")
                    .get(1)
                    .path("content")
                    .get(0)
                    .path("text");
            String realData=textNode.asString().replaceAll("```json\\n","")
                    .replaceAll("\\n```","").trim();
            log.info("Clean Response.. {}",realData);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //Start
        return null;
    }

    private String createPrompt(Activity activity) {

        return String.format("""
                        You are an experienced AI Fitness Coach.
                        
                        Analyze the following fitness activity and provide personalized recommendations.
                        Return ONLY valid JSON without any markdown, explanations, or extra text.
                        
                        Use the EXACT JSON structure below:
                        
                        {
                          "analysis": {
                            "overall": "Overall performance analysis",
                            "activity": "Analysis based on activity type",
                            "pace": "Pace or speed analysis",
                            "heartRate": "Heart rate analysis",
                            "caloriesBurned": "Calories burned analysis",
                            "distance": "Distance covered analysis",
                            "steps": "Steps analysis"
                          },
                          "improvements": [
                            {
                              "area": "Improvement area",
                              "recommendation": "Detailed recommendation"
                            }
                          ],
                          "nextWorkout": [
                            {
                              "workout": "Workout name",
                              "duration": "Recommended duration",
                              "description": "Workout description"
                            }
                          ],
                          "nutrition": {
                            "hydration": "Hydration recommendation",
                            "meal": "Post-workout meal recommendation"
                          },
                          "safety": [
                            "Safety recommendation 1",
                            "Safety recommendation 2"
                          ],
                          "summary": "Motivational summary for the user"
                        }
                        
                        Analyze the following activity details:
                        
                        User Information:
                        - Age: %s years
                        - Weight: %.1f kg
                        - Height: %.2f ft
                        
                        Activity Information:
                        - Activity Type: %s
                        - Duration: %d minutes
                        - Calories Burned: %d kcal
                        - Start Time: %s
                        
                        Additional Metrics:
                        %s
                        
                        Guidelines:
                        1. Personalize recommendations using all available metrics.
                        2. Evaluate heart rate, calories, duration, pace, distance, and steps.
                        3. Suggest realistic improvements.
                        4. Recommend the next workout based on today's performance.
                        5. Give hydration and nutrition advice.
                        6. Mention any safety precautions considering weather and heart rate.
                        7. Keep recommendations practical and motivating.
                        9. Simple English language so that user understand.
                        8. Return ONLY valid JSON.
                        """,
                activity.getAdditionalMetrics().get("age"),
                activity.getWeight(),
                activity.getHeight(),
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getStartTime(),
                activity.getAdditionalMetrics()
        );
    }
}
