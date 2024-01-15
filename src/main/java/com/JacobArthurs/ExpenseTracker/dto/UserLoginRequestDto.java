package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserLoginRequestDto {
    @NotBlank
    @Size(max = 50)
    private String userName;

    @NotBlank
    @Size(max = 200)
    private String password;
}
