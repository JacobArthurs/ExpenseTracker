package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ExpectedCategoryDistributionRequestDto {
    @NotNull
    private Long categoryId;

    @NotNull
    @Max(100)
    private int distribution;
}
