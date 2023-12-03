package com.JacobArthurs.ExpenseTracker.controller;
import com.JacobArthurs.ExpenseTracker.dto.ExpenseRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpenseDto;
import com.JacobArthurs.ExpenseTracker.service.ExpenseService;
import com.JacobArthurs.ExpenseTracker.util.ExpenseUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping()
    public ResponseEntity<List<ExpenseDto>> getAllExpenses() {
        var expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(ExpenseUtils.convertObjectListToDtoList(expenses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDto> getExpenseById(@PathVariable Long id) {
        var expense = expenseService.getExpenseById(id);
        if (expense != null) {
            return ResponseEntity.ok(new ExpenseDto(expense));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ExpenseDto> createExpense(@RequestBody @Valid ExpenseRequestDto request) {
        var expense = expenseService.createExpense(request);
        return ResponseEntity.ok(new ExpenseDto(expense));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable Long id, @RequestBody @Valid ExpenseRequestDto request) {
        var expense = expenseService.updateExpense(id, request);
        if (expense != null) {
            return ResponseEntity.ok(new ExpenseDto(expense));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        var deleted = expenseService.deleteExpense(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
