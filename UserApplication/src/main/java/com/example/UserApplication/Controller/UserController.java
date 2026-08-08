package com.example.UserApplication.Controller;

import com.example.UserApplication.DTO.RegisterRequest;
import com.example.UserApplication.DTO.UserResponse;
import com.example.UserApplication.Services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.getUsers(id));
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok().body(userService.register(registerRequest));
    }
    @GetMapping("/{keycloakId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String keycloakId){
        return ResponseEntity.ok().body(userService.existByKeycloakId(keycloakId));
    }
}
