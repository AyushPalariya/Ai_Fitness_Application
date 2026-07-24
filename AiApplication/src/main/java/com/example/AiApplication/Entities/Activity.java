package com.example.AiApplication.Entities;

import com.example.AiApplication.Entities.ActivityType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Activity {
    private Long id;
    private Long userId;
    private ActivityType type;
    private Long duration;
    private Long caloriesBurned;
    private LocalDateTime startTime;
    @Column(name="metrics",columnDefinition = "JSON")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String,Object> additionalMetrics;
    private Double weight;
    private Double height;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;
}
