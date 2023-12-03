package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CategoryRequestDto {
    @NotNull
    @Size(max = 50)
    private String title;

    @NotNull
    @Size(max = 200)
    private String description;
}
