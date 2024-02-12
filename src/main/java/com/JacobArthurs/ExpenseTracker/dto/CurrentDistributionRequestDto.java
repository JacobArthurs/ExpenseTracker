package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CurrentDistributionRequestDto {
    @NotNull
    private int monthCount;
}
