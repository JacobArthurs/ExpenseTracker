package com.JacobArthurs.ExpenseTracker.util;

import com.JacobArthurs.ExpenseTracker.dto.CategoryDto;
import com.JacobArthurs.ExpenseTracker.dto.CategoryRequestDto;
import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.service.ExpenseService;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryUtils {
    public static List<CategoryDto> convertCategoryListToCategoryDtoList(List<Category> categories) {
        return categories.stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    public static Category convertCategoryRequestToCategory(CategoryRequestDto categoryRequest) {
        var category = new Category();
        category.setTitle(categoryRequest.getTitle());
        category.setDescription(categoryRequest.getDescription());
        category.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        return category;
    }
}
