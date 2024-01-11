package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.ExpenseRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpenseSearchRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.PaginatedResponse;
import com.JacobArthurs.ExpenseTracker.model.Expense;
import com.JacobArthurs.ExpenseTracker.repository.ExpenseRepository;
import com.JacobArthurs.ExpenseTracker.util.ExpenseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

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

    public PaginatedResponse<Expense> searchExpenses(ExpenseSearchRequestDto request) {
        Specification<Expense> spec = Specification.where(null);

        if (request.getId() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("id"), request.getId()));
        }

        if (request.getCategoryId() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("categoryId"), request.getCategoryId()));
        }

        if (request.getTitle() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + request.getTitle().toLowerCase() + "%"));
        }

        if (request.getDescription() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + request.getDescription().toLowerCase() + "%"));
        }

        if (request.getOverviewText() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + request.getOverviewText().toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + request.getOverviewText().toLowerCase() + "%")
                    )
            );
        }

        if (request.getCreatedDate() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("createdDate"), request.getCreatedDate()));
        }

        if (request.getLastUpdatedDate() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("lastUpdatedDate"), request.getLastUpdatedDate()));
        }

        Pageable pageable = PageRequest.of(request.getOffset(), request.getLimit());

        Page<Expense> expensePage = expenseRepository.findAll(spec, pageable);

        return new PaginatedResponse<>(request.getLimit(), request.getOffset(), expensePage.getTotalElements(), expensePage.getContent());
    }
}
