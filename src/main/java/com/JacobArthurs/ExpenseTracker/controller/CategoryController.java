package com.JacobArthurs.ExpenseTracker.controller;

import com.JacobArthurs.ExpenseTracker.dto.CategoryDto;
import com.JacobArthurs.ExpenseTracker.dto.CategoryRequestDto;
import com.JacobArthurs.ExpenseTracker.service.CategoryService;
import com.JacobArthurs.ExpenseTracker.util.CategoryUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        var categories = categoryService.getAllCategories();
        return ResponseEntity.ok(CategoryUtils.convertObjectListToDtoList(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        var category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(new CategoryDto(category));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryRequestDto request) {
        var category = categoryService.createCategory(request);
        return ResponseEntity.ok(new CategoryDto(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequestDto request) {
        var category = categoryService.updateCategory(id, request);
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
