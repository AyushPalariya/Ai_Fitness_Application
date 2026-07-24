package com.example.ActivityApplication.DTO;

import com.example.ActivityApplication.Entities.ActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;
@Data
public class ActivityRequest {
    @NotNull(message= "userId required")
    private Long userId;
    @NotNull(message="Select activity type")
    private ActivityType type;
    @NotNull(message = "weight is required in Kg")
    @Positive(message = "weight must be greater than 0")
    private Double weight;
    @NotNull(message = "height is required in feet")
    private Double height;
    @NotNull(message = "duration is required")
    private Long duration;
    @NotNull(message = "calories required")
    private Long caloriesBurned;
    private LocalDateTime startTime;
    private Map<String,Object> additionalMetrics;

}
