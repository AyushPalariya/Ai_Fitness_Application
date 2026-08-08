package com.example.ActivityApplication.Services;

import com.example.ActivityApplication.DTO.ActivityRequest;
import com.example.ActivityApplication.DTO.ActivityResponse;
import com.example.ActivityApplication.Entities.Activity;
import com.example.ActivityApplication.Repository.ActivityRepo;
import com.example.ActivityApplication.Repository.UserFiegn;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {
    private final UserFiegn userFiegn;
    private final ActivityRepo activityRepo;
    private final KafkaTemplate<String,Activity> kafkaTemplate;
    @Value("${kafka.topic.name}")
    private String topicName;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        boolean isValidUser=userFiegn.validateUser(activityRequest.getKeycloakId());
        if(!isValidUser){
            log.info("user isNotValid working..");
            throw new RuntimeException("Invalid User with keycloakId: "+ activityRequest.getKeycloakId());
        }
        Activity activity=new Activity();
        activity.setUserId(activityRequest.getUserId());
        activity.setAdditionalMetrics(activityRequest.getAdditionalMetrics());
        activity.setCaloriesBurned(activityRequest.getCaloriesBurned());
        activity.setDuration(activityRequest.getDuration());
        activity.setType(activityRequest.getType());
        activity.setStartTime(activityRequest.getStartTime());
        activity.setWeight(activityRequest.getWeight());
        activity.setHeight(activityRequest.getHeight());
        activity.setKeycloakId(activityRequest.getKeycloakId());

        Activity saved=activityRepo.save(activity);
        //send details in kafka
        try {
            kafkaTemplate.send(topicName, String.valueOf(saved.getUserId()), saved);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //
        return new ActivityResponse(saved.getId(), saved.getUserId(),saved.getKeycloakId() ,saved.getType(), saved.getDuration(),
                saved.getCaloriesBurned(),saved.getStartTime(),saved.getWeight(),saved.getHeight(),saved.getAdditionalMetrics(),saved.getCreatedAt(),saved.getUpdateAt());

    }
}
