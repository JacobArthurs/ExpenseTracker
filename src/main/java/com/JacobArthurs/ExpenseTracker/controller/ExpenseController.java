package com.JacobArthurs.ExpenseTracker.controller;
import com.JacobArthurs.ExpenseTracker.dto.ExpenseRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpenseDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpenseSearchRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.PaginatedResponse;
import com.JacobArthurs.ExpenseTracker.service.ExpenseService;
import com.JacobArthurs.ExpenseTracker.util.ExpenseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
@Tag(name="Expense controller", description = "CRUD endpoints for expenses.")
public class ExpenseController {
    private final ExpenseService expenseService;

    @Autowired
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

    @PostMapping("/Search")
    @Operation(summary = "Search expenses with pagination", description = "Returns paginated expenses based on search criteria")
    public ResponseEntity<PaginatedResponse<ExpenseDto>> getAllExpenses(@RequestBody @Valid ExpenseSearchRequestDto request) {
        var expenses = expenseService.searchExpenses(request);
        return ResponseEntity.ok(ExpenseUtil.convertPaginatedToPaginatedDto(expenses));
    }

    @PostMapping
    @Operation(summary = "Create an expense", description = "Creates an expense as per the request body")
    public ResponseEntity<ExpenseDto> createExpense(@RequestBody @Valid ExpenseRequestDto request) {
        var expense = expenseService.createExpense(request);
        return ResponseEntity.ok(new ExpenseDto(expense));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an expense", description = "Updates an expense as per the id and request body")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable Long id, @RequestBody @Valid ExpenseRequestDto request) {
        var expense = expenseService.updateExpense(id, request);
        if (expense != null) {
            return ResponseEntity.ok(new ExpenseDto(expense));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an expense", description = "Deletes an expense as per the id")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        var deleted = expenseService.deleteExpense(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
