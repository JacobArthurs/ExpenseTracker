package com.JacobArthurs.ExpenseTracker.controller;

import com.JacobArthurs.ExpenseTracker.dto.*;
import com.JacobArthurs.ExpenseTracker.service.CategoryService;
import com.JacobArthurs.ExpenseTracker.util.CategoryUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@Tag(name="Category controller", description = "CRUD endpoints for categories.")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id", description = "Returns a category as per the id")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        var category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(new CategoryDto(category));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/search")
    @Operation(summary = "Search categories with pagination", description = "Returns paginated categories based on search criteria")
    public ResponseEntity<PaginatedResponse<CategoryDto>> getAllExpenses(@RequestBody @Valid CategorySearchRequestDto request) {
        var categories = categoryService.searchCategories(request);
        return ResponseEntity.ok(CategoryUtil.convertPaginatedToPaginatedDto(categories));
    }

    @PostMapping
    @Operation(summary = "Create a category", description = "Creates a category as per the request body")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryRequestDto request) {
        var category = categoryService.createCategory(request);
        return ResponseEntity.ok(new CategoryDto(category));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category", description = "Updates a category as per the id and request body")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequestDto request) {
        var category = categoryService.updateCategory(id, request);
        if (category != null) {
            return ResponseEntity.ok(new CategoryDto(category));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "Deletes a category as per the id")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        var deleted = categoryService.deleteCategory(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
