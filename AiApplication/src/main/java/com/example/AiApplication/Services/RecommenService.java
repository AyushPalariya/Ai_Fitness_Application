package com.example.AiApplication.Services;

import com.example.AiApplication.Entities.Recommendation;
import com.example.AiApplication.Repository.RecommenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommenService {
    private final RecommenRepo recommenRepo;
    public List<Recommendation> getUserRec(Long userId) {
        return recommenRepo.findByUserId((userId));
    }

    public Recommendation getActivityRec(Long id) {
        return recommenRepo.findByActivityId(id).orElseThrow(()->new RuntimeException("No Recommendation of "+id+" activity_id"));
    }
}
