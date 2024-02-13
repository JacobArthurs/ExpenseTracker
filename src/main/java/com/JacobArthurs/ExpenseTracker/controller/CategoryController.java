package com.JacobArthurs.ExpenseTracker.controller;

import com.JacobArthurs.ExpenseTracker.dto.*;
import com.JacobArthurs.ExpenseTracker.service.CategoryService;
import com.JacobArthurs.ExpenseTracker.util.PaginationUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@Tag(name = "Category controller", description = "CRUD endpoints for categories.")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Returns all categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        var categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories.stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList()));
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
        return ResponseEntity.ok(PaginationUtil.convertPaginatedToPaginatedDto(categories, CategoryDto::new));
    }

    @PostMapping
    @Operation(summary = "Create a category", description = "Creates a category as per the request body")
    public ResponseEntity<OperationResult> createCategory(@RequestBody @Valid CategoryRequestDto request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category", description = "Updates a category as per the id and request body")
    public ResponseEntity<OperationResult> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequestDto request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "Deletes a category as per the id")
    public ResponseEntity<OperationResult> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
