package com.example.UserApplication.Repository;

import com.example.UserApplication.Entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Boolean existsByEmail(String email);


    User findByEmail(String email);
    Boolean existsByKeycloakId(String keycloakId);
}
