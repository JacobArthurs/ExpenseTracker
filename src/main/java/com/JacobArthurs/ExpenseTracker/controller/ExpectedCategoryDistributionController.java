package com.JacobArthurs.ExpenseTracker.controller;

import com.JacobArthurs.ExpenseTracker.dto.*;
import com.JacobArthurs.ExpenseTracker.service.ExpectedCategoryDistributionService;
import com.JacobArthurs.ExpenseTracker.util.ExpectedCategoryDistributionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expected-category-distribution")
@Tag(name="Expected category distribution controller", description = "CRUD endpoints for expected category distributions.")
public class ExpectedCategoryDistributionController {
    private final ExpectedCategoryDistributionService expectedCategoryDistributionService;

    @Autowired
    public ExpectedCategoryDistributionController(ExpectedCategoryDistributionService expectedCategoryDistributionService) {
        this.expectedCategoryDistributionService = expectedCategoryDistributionService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an expected category distribution by id", description = "Returns an expected category distribution as per the id")
    public ResponseEntity<ExpectedCategoryDistributionDto> getExpectedCategoryDistributionById(@PathVariable Long id) {
        var category = expectedCategoryDistributionService.getExpectedCategoryDistributionById(id);
        if (category != null) {
            return ResponseEntity.ok(new ExpectedCategoryDistributionDto(category));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all expected category distributions", description = "Returns all expected category distributions")
    public ResponseEntity<DistributionDto> getAllExpectedCategoryDistributions() {
        return ResponseEntity.ok(expectedCategoryDistributionService.getAllDistributions());
    }

    @PostMapping("/search")
    @Operation(summary = "Search expected category distributions with pagination", description = "Returns paginated expected category distributions based on search criteria")
    public ResponseEntity<PaginatedResponse<ExpectedCategoryDistributionDto>> getAllExpenses(@RequestBody @Valid ExpectedCategoryDistributionSearchRequestDto request) {
        var expectedCategoryDistributions = expectedCategoryDistributionService.searchExpectedCategoryDistributions(request);
        return ResponseEntity.ok(ExpectedCategoryDistributionUtil.convertPaginatedToPaginatedDto(expectedCategoryDistributions));
    }

    @PostMapping
    @Operation(summary = "Create an expected category distribution", description = "Creates an expected category distribution as per the request body")
    public ResponseEntity<ExpectedCategoryDistributionDto> createExpectedCategoryDistribution(@RequestBody @Valid ExpectedCategoryDistributionRequestDto request) {
        var category = expectedCategoryDistributionService.createExpectedCategoryDistribution(request);
        return ResponseEntity.ok(new ExpectedCategoryDistributionDto(category));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an expected category distribution", description = "Updates an expected category distribution as per the id and request body")
    public ResponseEntity<ExpectedCategoryDistributionDto> updateExpectedCategoryDistribution(@PathVariable Long id, @RequestBody @Valid ExpectedCategoryDistributionRequestDto request) {
        var category = expectedCategoryDistributionService.updateExpectedCategoryDistribution(id, request);
        if (category != null) {
            return ResponseEntity.ok(new ExpectedCategoryDistributionDto(category));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an expected category distribution", description = "Deletes an expected category distribution as per the id")
    public ResponseEntity<Void> deleteExpectedCategoryDistribution(@PathVariable Long id) {
        var deleted = expectedCategoryDistributionService.deleteExpectedCategoryDistribution(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
