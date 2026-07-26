package com.example.AiApplication.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor

public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long activityId;
    private String type;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String recommendation;
    //check here
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> improvements;
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> suggestion;
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> safety;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String nutrition;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String summary;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Recommendation(Long userId, Long activityId, String type,
                          String recommendation, List<String> improvements,
                          List<String> suggestion, List<String> safety, String nutrition, String summary) {
        this.userId=userId;
        this.activityId=activityId;
        this.type=type;
        this.recommendation=recommendation;
        this.improvements=improvements;
        this.suggestion=suggestion;
        this.safety=safety;
        this.nutrition=nutrition;
        this.summary=summary;
    }
}
