package com.JacobArthurs.ExpenseTracker.controller;
import com.JacobArthurs.ExpenseTracker.dto.*;
import com.JacobArthurs.ExpenseTracker.service.ExpenseService;
import com.JacobArthurs.ExpenseTracker.util.ExpenseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/expense")
@Tag(name="Expense controller", description = "CRUD endpoints for expenses.")
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an expense by id", description = "Returns an expense as per the id")
    public ResponseEntity<ExpenseDto> getExpenseById(@PathVariable Long id) {
        var expense = expenseService.getExpenseById(id);
        if (expense != null) {
            return ResponseEntity.ok(new ExpenseDto(expense));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/monthly-metric")
    @Operation(summary = "Get monthly expense metric data", description = "Returns an objecting containing monthly expense metrics")
    public ResponseEntity<MonthlyExpenseMetricDto> getMonthlyExpenseMetric() {
        return ResponseEntity.ok(expenseService.getMonthlyExpenseMetric());
    }

    @PostMapping("/total-amount")
    @Operation(summary = "Get total amount of expenses", description = "Returns total amount of expenses in the current month")
    public ResponseEntity<BigDecimal> getTotalAmount(@RequestBody @Valid TotalExpenseAmountRequestDto request) {
        return ResponseEntity.ok(expenseService.getTotalExpenseAmount(request));
    }

    @PostMapping("/current-distribution")
    @Operation(summary = "Get the current distribution", description = "Returns current distributions based on the provided start date")
    public ResponseEntity<DistributionDto> getCurrentDistribution(@RequestBody @Valid CurrentDistributionRequestDto request) {
        return ResponseEntity.ok(expenseService.getCurrentDistribution(request));
    }

    @PostMapping("/search")
    @Operation(summary = "Search expenses with pagination", description = "Returns paginated expenses based on search criteria")
    public ResponseEntity<PaginatedResponse<ExpenseDto>> getAllExpenses(@RequestBody @Valid ExpenseSearchRequestDto request) {
        var expenses = expenseService.searchExpenses(request);
        return ResponseEntity.ok(ExpenseUtil.convertPaginatedToPaginatedDto(expenses));
    }

    @PostMapping
    @Operation(summary = "Create an expense", description = "Creates an expense as per the request body")
    public ResponseEntity<OperationResult> createExpense(@RequestBody @Valid ExpenseRequestDto request) {
        return ResponseEntity.ok(expenseService.createExpense(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an expense", description = "Updates an expense as per the id and request body")
    public ResponseEntity<OperationResult> updateExpense(@PathVariable Long id, @RequestBody @Valid ExpenseRequestDto request) {
        return ResponseEntity.ok(expenseService.updateExpense(id, request));
    }
    @PutMapping("/category-reassign")
    @Operation(summary = "Move Expenses to New Category", description = "Updates expenses categories from one category to another category.")
    public ResponseEntity<OperationResult> reassignExpensesToNewCategory(@RequestBody @Valid ReassignExpenseCategoryRequestDto request) {
        return ResponseEntity.ok(expenseService.reassignExpensesToNewCategory(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an expense", description = "Deletes an expense as per the id")
    public ResponseEntity<OperationResult> deleteExpense(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.deleteExpense(id));
    }
}
