package com.gkrs.fintrek.fintrek_app.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank(message = "Name cannot be null")
    @Size(min = 5, max = 50, message = "User name must be at least 5-50 characters")
    private String fullName;

    @Email(message = "Please enter a valid email")
    @NotBlank(message = "Email cant be null")
    private String email;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 5, message = "Password size must be at least 6 characters")
    private String password;
}
