package com.example.ActivityApplication.Controller;

import com.example.ActivityApplication.DTO.ActivityRequest;
import com.example.ActivityApplication.DTO.ActivityResponse;
import com.example.ActivityApplication.Services.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class ActivityController {

    private final ActivityService activityService;
    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest activityRequest){
        return ResponseEntity.ok().body(activityService.trackActivity(activityRequest));
    }
}
