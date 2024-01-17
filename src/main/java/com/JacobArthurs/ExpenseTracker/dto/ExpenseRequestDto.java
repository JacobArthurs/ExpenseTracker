package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ExpenseRequestDto {
    @NotNull
    private Long categoryId;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 200)
    private String description;
}
