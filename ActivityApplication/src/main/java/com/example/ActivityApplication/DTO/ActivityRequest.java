package com.example.ActivityApplication.DTO;

import com.example.ActivityApplication.Entities.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
@Data
public class ActivityRequest {
    private Long userId;
    private ActivityType type;
    private Long duration;
    private Long caloriesBurned;
    private LocalDateTime startTime;
    private Map<String,Object> additionalMetrics;

}
