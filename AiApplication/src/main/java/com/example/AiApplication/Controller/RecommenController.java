package com.example.AiApplication.Controller;

import com.example.AiApplication.Entities.Recommendation;
import com.example.AiApplication.Services.RecommenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class RecommenController {

    private final RecommenService recommenService;
    @GetMapping("/user/userId")
    public ResponseEntity<List<Recommendation>> getUserRecommendation(@PathVariable Long userId){
        return ResponseEntity.ok().body(recommenService.getUserRec(userId));
    }
    @GetMapping("/activity/{id}")
    public ResponseEntity<Recommendation> getActivityRecommendation(@PathVariable Long id){
        return ResponseEntity.ok().body(recommenService.getActivityRec(id));
    }


}
