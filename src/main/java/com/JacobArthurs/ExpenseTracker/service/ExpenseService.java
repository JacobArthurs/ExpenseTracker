package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.*;
import com.JacobArthurs.ExpenseTracker.enumerator.UserRole;
import com.JacobArthurs.ExpenseTracker.model.Expense;
import com.JacobArthurs.ExpenseTracker.repository.ExpenseRepository;
import com.JacobArthurs.ExpenseTracker.util.ExpenseUtil;
import com.JacobArthurs.ExpenseTracker.util.OffsetBasedPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.TreeMap;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    public final CurrentUserProvider currentUserProvider;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, CategoryService categoryService, CurrentUserProvider currentUserProvider) {
        this.expenseRepository = expenseRepository;
        this.categoryService = categoryService;
        this.currentUserProvider = currentUserProvider;
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id) {
        var expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with ID: " + id));

        if (doesCurrentUserNotOwnExpense(expense))
            throw new RuntimeException("You are not authorized to get an expense that is not yours.");
        else
            return expense;
    }

    public Expense createExpense(ExpenseRequestDto request) {
        var expense = ExpenseUtil.convertRequestToObject(request, categoryService);

        expense.setCreatedBy(currentUserProvider.getCurrentUser());
        expense.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long id, ExpenseRequestDto request) {
        var expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with ID: " + id));;

        if (doesCurrentUserNotOwnExpense(expense))
            throw new RuntimeException("You are not authorized to update an expense that is not yours.");

        expense.setCategory(categoryService.getCategoryById(request.getCategoryId()));
        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        return expenseRepository.save(expense);
    }

    public boolean deleteExpense(Long id) {
        var expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with ID: " + id));
        if (doesCurrentUserNotOwnExpense(expense))
            throw new RuntimeException("You are not authorized to delete an expense that is not yours.");

        expenseRepository.deleteById(id);
        return true;
    }

    public PaginatedResponse<Expense> searchExpenses(ExpenseSearchRequestDto request) {
        Specification<Expense> spec = Specification.where(null);

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("createdBy"), currentUserProvider.getCurrentUser()));

        if (request.getId() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("id"), request.getId()));
        }

        if (request.getCategoryId() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("category").get("id"), request.getCategoryId()));
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

        if (request.getStartDate() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), request.getStartDate()));
        }

        if (request.getEndDate() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), request.getEndDate()));
        }

        var sort = Sort.by(
                Sort.Order.desc("createdDate"),
                Sort.Order.desc("lastUpdatedDate"),
                Sort.Order.asc("id"));

        Pageable pageable = new OffsetBasedPageRequest(request.getOffset(), request.getLimit(), sort);

        Page<Expense> expensePage = expenseRepository.findAll(spec, pageable);

        return new PaginatedResponse<>(request.getLimit(), request.getOffset(), expensePage.getTotalElements(), expensePage.getContent());
    }

    public DistributionDto getCurrentDistribution (CurrentDistributionRequestDto request) {
        var startDate = Timestamp.valueOf(request.getCurrentDate().toLocalDateTime().minusMonths(1));

        Specification<Expense> spec = Specification.where(null);
        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdDate"), startDate, request.getCurrentDate()));

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("createdBy"), currentUserProvider.getCurrentUser()));

        var sort = Sort.by(Sort.Order.asc("category.id"));

        var expenses = expenseRepository.findAll(spec, sort);

        var groupedExpenses = expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getTitle(),
                        TreeMap::new,
                        Collectors.toList()
                ));

        var categories = expenses.stream()
                .map(expense -> expense.getCategory().getTitle())
                .distinct()
                .toList();

        var totalCount = groupedExpenses.values().stream()
                .mapToLong(List::size)
                .sum();

        var distributions = categories.stream()
                .map(key -> (int) ((double) groupedExpenses.get(key).size() / totalCount * 100))
                .toList();

        return new DistributionDto(categories, distributions);
    }

    private boolean doesCurrentUserNotOwnExpense(Expense expense) {
        var currentUser = currentUserProvider.getCurrentUser();
        return !Objects.equals(expense.getCreatedBy().getId(), currentUser.getId()) &&
                !UserRole.ADMIN.equals(currentUser.getRole());
    }
}
