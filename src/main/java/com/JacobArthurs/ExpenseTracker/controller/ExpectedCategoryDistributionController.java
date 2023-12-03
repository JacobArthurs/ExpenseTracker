package com.JacobArthurs.ExpenseTracker.controller;

import com.JacobArthurs.ExpenseTracker.dto.CategoryDto;
import com.JacobArthurs.ExpenseTracker.dto.CategoryRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpectedCategoryDistributionDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpectedCategoryDistributionRequestDto;
import com.JacobArthurs.ExpenseTracker.service.CategoryService;
import com.JacobArthurs.ExpenseTracker.service.ExpectedCategoryDistributionService;
import com.JacobArthurs.ExpenseTracker.util.CategoryUtils;
import com.JacobArthurs.ExpenseTracker.util.ExpectedCategoryDistributionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expected-category-distribution")
public class ExpectedCategoryDistributionController {
    private final ExpectedCategoryDistributionService expectedCategoryDistributionService;

    @Autowired
    public ExpectedCategoryDistributionController(ExpectedCategoryDistributionService expectedCategoryDistributionService) {
        this.expectedCategoryDistributionService = expectedCategoryDistributionService;
    }

    @GetMapping()
    public ResponseEntity<List<ExpectedCategoryDistributionDto>> getAllExpectedCategoryDistributions() {
        var categories = expectedCategoryDistributionService.getAllCategories();
        return ResponseEntity.ok(ExpectedCategoryDistributionUtils.convertObjectListToDtoList(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpectedCategoryDistributionDto> getExpectedCategoryDistributionById(@PathVariable Long id) {
        var category = expectedCategoryDistributionService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(new ExpectedCategoryDistributionDto(category));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ExpectedCategoryDistributionDto> createExpectedCategoryDistribution(@RequestBody ExpectedCategoryDistributionRequestDto request) {
        var category = expectedCategoryDistributionService.createCategory(request);
        return ResponseEntity.ok(new ExpectedCategoryDistributionDto(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpectedCategoryDistributionDto> updateExpectedCategoryDistribution(@PathVariable Long id, @RequestBody ExpectedCategoryDistributionRequestDto request) {
        var category = expectedCategoryDistributionService.updateCategory(id, request);
        if (category != null) {
            return ResponseEntity.ok(new ExpectedCategoryDistributionDto(category));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpectedCategoryDistribution(@PathVariable Long id) {
        var deleted = expectedCategoryDistributionService.deleteCategory(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
