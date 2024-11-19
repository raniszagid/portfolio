package com.zahid.task_management_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AuthenticationDTO {
    @Email
    @NotNull
    @NotEmpty
    private String username;
    private String password;
}
