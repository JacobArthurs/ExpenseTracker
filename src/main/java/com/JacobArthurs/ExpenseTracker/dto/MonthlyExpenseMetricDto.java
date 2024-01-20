package com.JacobArthurs.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlyExpenseMetricDto {
    private List<String> months;
    private List<BigDecimal> amounts;
}
