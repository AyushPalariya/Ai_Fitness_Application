package com.example.ActivityApplication.Repository;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserFiegn {
    @LoadBalanced
    @GetMapping("/api/users/{keycloakId}/validate")
    Boolean validateUser(@PathVariable String keycloakId);
}
