package com.example.UserApplication.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Please enter field")
    private String firstName;
    @NotBlank(message = "Please enter field")
    private String lastName;
    @NotBlank(message = "Password is required")
    @Size(min=6,message = "Password must have atleast 6 characters")
    private String password;
}
