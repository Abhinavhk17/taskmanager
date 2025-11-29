package com.api.taskmanager.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequest {
    private String firstName;
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    private String phone;
}

