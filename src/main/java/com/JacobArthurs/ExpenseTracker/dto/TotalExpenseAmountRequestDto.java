package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class TotalExpenseAmountRequestDto {
    @NotNull
    private Timestamp month;
}
