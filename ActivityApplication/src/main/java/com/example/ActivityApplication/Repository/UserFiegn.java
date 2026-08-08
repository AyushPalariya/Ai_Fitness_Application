package com.example.ActivityApplication.Repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserFiegn {
    @GetMapping("/api/users/{userId}/validate")
    boolean validateUser(@PathVariable Long userId);
}
