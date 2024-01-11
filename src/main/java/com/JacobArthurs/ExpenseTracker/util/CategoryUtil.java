package com.JacobArthurs.ExpenseTracker.util;

import com.JacobArthurs.ExpenseTracker.dto.CategoryDto;
import com.JacobArthurs.ExpenseTracker.dto.CategoryRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpenseDto;
import com.JacobArthurs.ExpenseTracker.dto.PaginatedResponse;
import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.model.Expense;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryUtil {
    public static PaginatedResponse<CategoryDto> convertPaginatedToPaginatedDto(PaginatedResponse<Category> paginatedCategory) {
        List<CategoryDto> categoryDtos = paginatedCategory.getData().stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(paginatedCategory.getLimit(), paginatedCategory.getOffset(), paginatedCategory.getTotal(), categoryDtos);
    }

    public static Category convertRequestToObject(CategoryRequestDto request) {
        var category = new Category();
        category.setTitle(request.getTitle());
        category.setDescription(request.getDescription());
        category.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        return category;
    }
}
