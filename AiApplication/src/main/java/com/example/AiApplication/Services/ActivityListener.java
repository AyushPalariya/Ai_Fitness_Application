package com.example.AiApplication.Services;

import com.example.AiApplication.Entities.Activity;
import com.example.AiApplication.Entities.Recommendation;
import com.example.AiApplication.Repository.RecommenRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityListener {
    private final ActivityAiGemini activityAiGemini;
    private final RecommenRepo recommenRepo;

    @KafkaListener(topics = "${kafka.topic.name}",groupId = "activity-processor-group")
    public void processActivity(Activity activity){
        log.info("Received activity for processing: {}", activity.getUserId());
        Recommendation recommendation = activityAiGemini.generateRecommendation(activity);
        log.info("Database saving point");
        recommenRepo.save(recommendation);
    }
}
