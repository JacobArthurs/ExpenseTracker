package com.JacobArthurs.ExpenseTracker.util;

import com.JacobArthurs.ExpenseTracker.dto.ExpenseRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpenseDto;
import com.JacobArthurs.ExpenseTracker.dto.PaginatedResponse;
import com.JacobArthurs.ExpenseTracker.model.Expense;
import com.JacobArthurs.ExpenseTracker.service.CategoryService;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseUtil {
    public static PaginatedResponse<ExpenseDto> convertPaginatedToPaginatedDto(PaginatedResponse<Expense> paginatedExpense) {
        List<ExpenseDto> expenseDtos = paginatedExpense.getData().stream()
                .map(ExpenseDto::new)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(paginatedExpense.getLimit(), paginatedExpense.getOffset(), paginatedExpense.getTotal(), expenseDtos);
    }

    public static Expense convertRequestToObject(ExpenseRequestDto request, CategoryService categoryService) {
        var expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        var category = categoryService.getCategoryById(request.getCategoryId());
        expense.setCategory(category);

        return expense;
    }
}
