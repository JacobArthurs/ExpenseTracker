package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class TotalExpenseAmountRequestDto {
    @NotNull
    private Timestamp month;
}
