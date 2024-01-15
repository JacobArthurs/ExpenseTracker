package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRegisterRequestDto {
    @NotBlank
    @Size(max = 50)
    private String userName;

    @NotBlank
    @Size(max = 200)
    private String password;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Email
    @Size(max = 200)
    private String email;
}
