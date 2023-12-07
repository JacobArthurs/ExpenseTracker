package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.ExpenseRequestDto;
import com.JacobArthurs.ExpenseTracker.model.Expense;
import com.JacobArthurs.ExpenseTracker.repository.ExpenseRepository;
import com.JacobArthurs.ExpenseTracker.util.ExpenseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, CategoryService categoryService) {
        this.expenseRepository = expenseRepository;
        this.categoryService = categoryService;
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long  id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public Expense createExpense(ExpenseRequestDto request) {
        var expense = ExpenseUtil.convertRequestToObject(request, categoryService);
        expense.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long id, ExpenseRequestDto request) {
        if (expenseRepository.existsById(id)) {
            var expense = ExpenseUtil.convertRequestToObject(request, categoryService);
            expense.setId(id);

            return expenseRepository.save(expense);
        } else {
            return null;
        }
    }

    public boolean deleteExpense(Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
