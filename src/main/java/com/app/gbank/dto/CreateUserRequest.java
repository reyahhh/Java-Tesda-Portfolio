package com.app.gbank.dto;

import com.app.gbank.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100)
    String fullName,

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^09\\d{9}$", message = "Must be a valid PH mobile number")
    String mobileNumber,

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password,

    @NotNull(message = "Role is required")
    Role role
) {}