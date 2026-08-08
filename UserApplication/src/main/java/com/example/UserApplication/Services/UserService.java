package com.example.UserApplication.Services;

import com.example.UserApplication.DTO.RegisterRequest;
import com.example.UserApplication.DTO.UserResponse;
import com.example.UserApplication.Entities.User;
import com.example.UserApplication.Repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j//this is for logger
public class UserService {
    private final UserRepo userRepo;

    public UserResponse register(RegisterRequest registerRequest) {
        if(userRepo.existsByEmail(registerRequest.getEmail())){
            User existUser=userRepo.findByEmail(registerRequest.getEmail());
            UserResponse userResponse=new UserResponse();
            userResponse.setId(existUser.getId());
            userResponse.setKeycloakId(existUser.getKeycloakId());
            userResponse.setEmail(existUser.getEmail());
            userResponse.setCreatedAt(existUser.getCreatedAt());
            userResponse.setFirstName(existUser.getFirstName());
            userResponse.setLastName(existUser.getLastName());
            userResponse.setPassword(existUser.getPassword());
            userResponse.setUpdateAt(existUser.getUpdateAt());
            return userResponse;
        }
        User user=new User();
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPassword(registerRequest.getPassword());
        user.setKeycloakId(registerRequest.getKeycloakId());

        User saved=userRepo.save(user);
        UserResponse userResponse=new UserResponse();
        userResponse.setId(saved.getId());
        userResponse.setEmail(saved.getEmail());
        userResponse.setCreatedAt(saved.getCreatedAt());
        userResponse.setFirstName(saved.getFirstName());
        userResponse.setLastName(saved.getLastName());
        userResponse.setPassword(saved.getPassword());
        userResponse.setUpdateAt(saved.getUpdateAt());
        userResponse.setKeycloakId(saved.getKeycloakId());

        return userResponse;

    }

    public UserResponse getUsers(Long id) {
        User user=userRepo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        UserResponse userResponse=new UserResponse(user.getId(),user.getKeycloakId(), user.getFirstName(), user.getLastName(),user.getEmail(),user.getPassword()
                ,user.getCreatedAt(),user.getUpdateAt());
        return userResponse;
    }

    public Boolean existByKeycloakId(String keycloakId) {
        log.info("valid working userId{}",keycloakId);
        return userRepo.existsByKeycloakId(keycloakId);
    }
}
