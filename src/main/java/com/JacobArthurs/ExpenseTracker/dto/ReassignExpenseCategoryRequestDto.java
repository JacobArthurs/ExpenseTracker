package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReassignExpenseCategoryRequestDto {
    @NotNull
    private Long oldCategoryId;

    @NotNull
    private Long newCategoryId;
}
