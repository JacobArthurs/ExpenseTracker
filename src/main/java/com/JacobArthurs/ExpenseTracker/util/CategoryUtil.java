package com.JacobArthurs.ExpenseTracker.util;

import com.JacobArthurs.ExpenseTracker.dto.CategoryDto;
import com.JacobArthurs.ExpenseTracker.dto.CategoryRequestDto;
import com.JacobArthurs.ExpenseTracker.model.Category;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryUtil {
    public static List<CategoryDto> convertObjectListToDtoList(List<Category> categories) {
        return categories.stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    public static Category convertRequestToObject(CategoryRequestDto request) {
        var category = new Category();
        category.setTitle(request.getTitle());
        category.setDescription(request.getDescription());
        category.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        return category;
    }
}
