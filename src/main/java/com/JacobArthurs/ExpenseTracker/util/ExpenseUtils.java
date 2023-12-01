package com.JacobArthurs.ExpenseTracker.util;

import com.JacobArthurs.ExpenseTracker.dto.ExpenseDto;
import com.JacobArthurs.ExpenseTracker.model.Expense;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseUtils {
    public static List<ExpenseDto> convertToExpenseDtos(List<Expense> expenses) {
        return expenses.stream()
                .map(ExpenseDto::new) // Assuming ExpenseDto has an appropriate constructor
                .collect(Collectors.toList());
    }
}
