package com.JacobArthurs.ExpenseTracker.controller;

import com.JacobArthurs.ExpenseTracker.dto.CategoryDto;
import com.JacobArthurs.ExpenseTracker.dto.CategoryRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpenseDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpenseRequestDto;
import com.JacobArthurs.ExpenseTracker.service.CategoryService;
import com.JacobArthurs.ExpenseTracker.service.ExpenseService;
import com.JacobArthurs.ExpenseTracker.util.CategoryUtils;
import com.JacobArthurs.ExpenseTracker.util.ExpenseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) { this.categoryService = categoryService; }

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAllExpenses() {
        var categories = categoryService.getAllCategories();
        return ResponseEntity.ok(CategoryUtils.convertCategoryListToCategoryDtoList(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getExpenseById(@PathVariable Long id) {
        var category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(new CategoryDto(category));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createExpense(@RequestBody CategoryRequestDto categoryRequest) {
        var category = categoryService.createCategory(categoryRequest);
        return ResponseEntity.ok(new CategoryDto(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateExpense(@PathVariable Long id, @RequestBody CategoryRequestDto categoryRequest) {
        var category = categoryService.updateCategory(id, categoryRequest);
        if (category != null) {
            return ResponseEntity.ok(new CategoryDto(category));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        var deleted = categoryService.deleteCategory(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
