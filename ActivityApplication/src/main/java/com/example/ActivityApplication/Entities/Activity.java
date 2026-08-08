package com.example.ActivityApplication.Entities;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Map;
@Entity
@Data
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String keycloakId;
    @Enumerated(EnumType.STRING)
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
