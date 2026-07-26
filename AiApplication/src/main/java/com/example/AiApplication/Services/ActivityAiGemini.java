package com.example.AiApplication.Services;

import com.example.AiApplication.Entities.Activity;
import com.example.AiApplication.Entities.Recommendation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

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
            ObjectMapper mapper=new ObjectMapper();//used to convert json to object and object to json
            JsonNode rootNode=mapper.readTree(aiResponse);//goes to rootNode '{'
            JsonNode textNode= rootNode.path("steps")//goes to steps
                    .get(1)//get 1 index value from array
                    .path("content")
                    .get(0)
                    .path("text");
            String realData=textNode.asString().replaceAll("```json\\n","")
                    .replaceAll("\\n```","").trim();
            log.info("Clean Response.. {}",realData);//real data in json format

            //convert json into object

            //Analysis
            //"analysis": {
            //    "overall": "You completed a strong 30-minute morning run covering 4.5 km. Your performance shows good aerobic endurance and a consistent effort throughout the session.",
            //    "activity": "Running is great for your heart health, legs, and overall stamina. This was a solid cardio workout.",
            //    "pace": "Your average speed was 9.0 km/h (around 6 minutes and 40 seconds per kilometer), which is a steady and controlled pace.",
            //    "heartRate": "Your average heart rate was 145 bpm. This sits in a safe, healthy moderate intensity zone, great for burning fat and building endurance.",
            //    "caloriesBurned": "You burned 350 calories in 30 minutes, which is very efficient for your body weight of 72.5 kg.",
            //    "distance": "Covering 4.5 km in half an hour is a fantastic distance for a regular morning workout.",
            //    "steps": "Taking 6,200 steps gives you a quick leg turnaround (cadence), helping you maintain good forward movement."
            //  }
            JsonNode analysisJson=mapper.readTree(realData);
            JsonNode analysisNode=analysisJson.path("analysis");//analysisNode that having innerNode
            StringBuilder analysisSb=new StringBuilder();
            ConvertInHumanReadableFormat(analysisSb,analysisNode,"overall","Overall: ");
            ConvertInHumanReadableFormat(analysisSb,analysisNode,"activity","Activity: ");
            ConvertInHumanReadableFormat(analysisSb,analysisNode,"pace","Pace: ");
            ConvertInHumanReadableFormat(analysisSb,analysisNode,"heartRate","Heart Rate: ");
            ConvertInHumanReadableFormat(analysisSb,analysisNode,"caloriesBurned","Calories: ");
            ConvertInHumanReadableFormat(analysisSb,analysisNode,"distance","Distance: ");
            ConvertInHumanReadableFormat(analysisSb,analysisNode,"steps","Steps: ");

            //Improvement
            //"improvements": [
            //    {
            //      "area": "Interval Speed Training",
            //      "recommendation": "Try adding 30-second faster jogs every 5 minutes to help increase your overall running speed over time."
            //    },
            //    {
            //      "area": "Post-Run Mobility",
            //      "recommendation": "Spend 5 to 10 minutes stretching your calves, hamstrings, and hips after running to prevent stiffness."
            //    }
            //  ]
            List<String> improvements=extractImprovements(analysisJson.path("improvements"));
            // "nextWorkout": [
            //    {
            //      "workout": "Active Recovery & Core Workout",
            //      "duration": "35 minutes",
            //      "description": "A light 20-minute walk followed by 15 minutes of simple core exercises like planks and bird-dogs to support better running posture."
            //    }
            //  ]
            List<String> suggestion=extractNextWorkOut(analysisJson.path("nextWorkout"));
            // "nutrition": {
            //    "hydration": "Drink about 500 to 750 ml of water over the next hour to replace the fluid lost during your run.",
            //    "meal": "Eat a balanced morning meal within 45 minutes, such as eggs with whole-grain toast or oatmeal with fruit and protein powder."
            //  }
            JsonNode nutritionNode=analysisJson.path("nutrition");
            StringBuilder nutritionSb=new StringBuilder();
            ConvertInHumanReadableFormat(nutritionSb,nutritionNode,"hydration","Hydration: ");
            ConvertInHumanReadableFormat(nutritionSb,nutritionNode,"meal","Meal: ");
            //"safety": [
            //    "Warm up with 3 to 5 minutes of dynamic leg swings and light walking before you start running.",
            //    "Pay attention to summer morning heat and wear light, breathable clothes to stay cool."
            //  ]
            List<String> safety=extractSafety(analysisJson.path("safety"));
            // "summary": "Fantastic job on today's 4.5 km run! You kept your heart rate in a great target
            // zone and burned plenty of calories.Stay consistent, rest well, and keep up the amazing work!"
            JsonNode summaryNode=analysisJson.path("summary");
            StringBuilder summarySb=new StringBuilder();
            summarySb.append("Summary: ").append(summaryNode.asString()).append("\n");

            return new Recommendation(activity.getUserId(),activity.getId(),
                    activity.getType().toString(),analysisSb.toString(),improvements,suggestion
                    ,safety,nutritionSb.toString(),summarySb.toString());
        }
        catch (Exception e){
            e.printStackTrace();
            return new Recommendation(activity.getUserId(),activity.getId(),
                    activity.getType().toString(),"Unable to generate detailed analysis.",
                    List.of("Continue with your current routine."),
                    List.of("Continue with your previous suggestion.")
                    ,List.of("Continue according to yours."),
                    "Continue with previous nutrition.","No summary");
        }
    }

    private List<String> extractSafety(JsonNode safetyNode) {
        List<String> safety=new ArrayList<>();
        if(safetyNode.isArray()){
            safetyNode.forEach(item->{
                safety.add(item.asString());
            });
        }
        if(safety.isEmpty()) safety.add("There is no any safety for you.");
        return safety;
    }

    private List<String> extractNextWorkOut(JsonNode nextWorkoutNode) {
        List<String> nextWorkout=new ArrayList<>();
        if(nextWorkoutNode.isArray()){
            nextWorkoutNode.forEach(work->{
                String workout=work.path("workout").asString();
                String details=work.path("description").asString();
                nextWorkout.add(String.format("%s: %s",workout,details));
            });
        }
        if(nextWorkout.isEmpty()) nextWorkout.add("There is no any suggestion.");
        return nextWorkout;

    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements=new ArrayList<>();
        if(improvementsNode.isArray()){
            improvementsNode.forEach(improvementsN->{
                String area=improvementsN.path("area").asString();
                String details=improvementsN.path("recommendation").asString();
                improvements.add(String.format("%s: %s",area,details));
            });
        }
        if(improvements.isEmpty()) improvements.add("No specific improvement needed");
        return improvements;
    }

    private void ConvertInHumanReadableFormat(StringBuilder analysisSb, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){//missing hai kya to dega nhi lekin ! iske karan ture
            //"overall": "You completed a strong 30-minute"
            //Overall: You completed a strong 30-minute
            analysisSb.append(prefix).append(analysisNode.path(key).asString()).append("\n\n");
        }
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
