package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CategoryRequestDto {
    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 200)
    private String description;
}
