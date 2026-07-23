package com.example.ActivityApplication.Services;

import com.example.ActivityApplication.DTO.ActivityRequest;
import com.example.ActivityApplication.DTO.ActivityResponse;
import com.example.ActivityApplication.Entities.Activity;
import com.example.ActivityApplication.Repository.ActivityRepo;
import com.example.ActivityApplication.Repository.UserFiegn;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActivityService {
    private final UserFiegn userFiegn;
    private final ActivityRepo activityRepo;
    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        boolean isValidUser=userFiegn.validateUser(activityRequest.getUserId());
        if(!isValidUser){
            System.out.println("working..");
            throw new RuntimeException("Invalid User: "+ activityRequest.getUserId());
        }
        Activity activity=new Activity();
        activity.setUserId(activityRequest.getUserId());
        activity.setAdditionalMetrics(activityRequest.getAdditionalMetrics());
        activity.setCaloriesBurned(activityRequest.getCaloriesBurned());
        activity.setDuration(activityRequest.getDuration());
        activity.setType(activityRequest.getType());
        activity.setStartTime(activityRequest.getStartTime());

        Activity saved=activityRepo.save(activity);
        return new ActivityResponse(saved.getId(), saved.getUserId(), saved.getType(), saved.getDuration(),
                saved.getCaloriesBurned(),saved.getStartTime(),saved.getAdditionalMetrics(),saved.getCreatedAt(),saved.getUpdateAt());

    }
}
