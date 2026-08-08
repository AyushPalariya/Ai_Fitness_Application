package com.example.ActivityApplication.DTO;

import com.example.ActivityApplication.Entities.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ActivityResponse {
    private Long id;
    private Long userId;
    private String keycloakId;
    private ActivityType type;
    private Long duration;
    private Long caloriesBurned;
    private LocalDateTime startTime;
    private Double weight;
    private Double height;
    private Map<String,Object> additionalMetrics;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
