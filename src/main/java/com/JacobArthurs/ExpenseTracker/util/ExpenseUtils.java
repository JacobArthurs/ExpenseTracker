package com.JacobArthurs.ExpenseTracker.util;

import com.JacobArthurs.ExpenseTracker.dto.ExpenseRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpenseDto;
import com.JacobArthurs.ExpenseTracker.model.Expense;
import com.JacobArthurs.ExpenseTracker.service.CategoryService;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseUtils {
    public static List<ExpenseDto> convertExpenseListToExpenseDtoList(List<Expense> expenses) {
        return expenses.stream()
                .map(ExpenseDto::new)
                .collect(Collectors.toList());
    }

    public static Expense convertExpenseRequestToExpense(ExpenseRequestDto expenseCreate, CategoryService categoryService) {
        var expense = new Expense();
        expense.setTitle(expenseCreate.getTitle());
        expense.setDescription(expenseCreate.getDescription());
        expense.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        var category = categoryService.getCategoryById(expenseCreate.getCategoryId());
        expense.setCategory(category);

        return expense;
    }
}
