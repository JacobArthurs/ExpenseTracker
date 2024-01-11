package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PaginatedRequest {
    private int offset = 0;

    @NotNull
    @Min(value = 1, message = "Limit must be greater than zero")
    private int limit;
}
