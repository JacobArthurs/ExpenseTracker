package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.*;
import com.JacobArthurs.ExpenseTracker.enumerator.UserRole;
import com.JacobArthurs.ExpenseTracker.model.Expense;
import com.JacobArthurs.ExpenseTracker.repository.ExpenseRepository;
import com.JacobArthurs.ExpenseTracker.util.ExpenseUtil;
import com.JacobArthurs.ExpenseTracker.util.OffsetBasedPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    public final CurrentUserProvider currentUserProvider;

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

    public OperationResult createExpense(ExpenseRequestDto request) {
        var expense = ExpenseUtil.convertRequestToObject(request, categoryService);

        expense.setCreatedBy(currentUserProvider.getCurrentUser());
        expense.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        expenseRepository.save(expense);
        return new OperationResult(true, "Expense created successfully");
    }

    public OperationResult updateExpense(Long id, ExpenseRequestDto request) {
        var expense = expenseRepository.findById(id).orElse(null);

        if (expense == null)
            return new OperationResult(false, "Expense not found with ID: " + id);
        if (doesCurrentUserNotOwnExpense(expense))
            return new OperationResult(false, "You are not authorized to update an expense that is not yours.");

        expense.setCategory(categoryService.getCategoryById(request.getCategoryId()));
        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        expenseRepository.save(expense);
        return new OperationResult(true, "Expense updated successfully");
    }

    public OperationResult deleteExpense(Long id) {
        var expense = expenseRepository.findById(id).orElse(null);

        if (expense == null)
            return new OperationResult(false, "Expense not found with ID: " + id);
        if (doesCurrentUserNotOwnExpense(expense))
            return new OperationResult(false, "You are not authorized to delete an expense that is not yours.");

        expenseRepository.deleteById(id);
        return new OperationResult(true, "Expense deleted successfully");
    }

    public PaginatedResponse<Expense> searchExpenses(ExpenseSearchRequestDto request) {
        Specification<Expense> spec = Specification.where(null);

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("createdBy"), currentUserProvider.getCurrentUser()));

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

        if (request.getMinAmount() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), request.getMinAmount()));
        }

        if (request.getMaxAmount() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("amount"), request.getMaxAmount()));
        }

        var sort = Sort.by(
                Sort.Order.desc("createdDate"),
                Sort.Order.desc("lastUpdatedDate"),
                Sort.Order.asc("id"));

        Pageable pageable = new OffsetBasedPageRequest(request.getOffset(), request.getLimit(), sort);

        Page<Expense> expensePage = expenseRepository.findAll(spec, pageable);

        return new PaginatedResponse<>(request.getLimit(), request.getOffset(), expensePage.getTotalElements(), expensePage.getContent());
    }

    public List<DistributionDto> getCurrentDistribution (CurrentDistributionRequestDto request) {
        var startDate = Timestamp.valueOf(request.getCurrentDate().toLocalDateTime().minusMonths(1));

        Specification<Expense> spec = Specification.where(null);
        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdDate"), startDate, request.getCurrentDate()));

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("createdBy"), currentUserProvider.getCurrentUser()));

        var sort = Sort.by(Sort.Order.asc("category.id"));

        var expenses = expenseRepository.findAll(spec, sort);
        var categories = categoryService.getAllCategories();

        var groupedExpenses = expenses.stream()
                .collect(Collectors.groupingBy(expense -> expense.getCategory().getId()));

        var totalCount = expenses.size();

        var groupedCategories = categories.stream()
                .collect(Collectors.toMap(
                        category -> category,
                        category -> groupedExpenses.getOrDefault(category.getId(), List.of())
                ));

        return groupedCategories.keySet().stream()
                .map(category -> new DistributionDto(
                        category.getExpectedCategoryDistribution() != null ? category.getExpectedCategoryDistribution().getId() : 0L,
                        category.getTitle(),
                        (int) ((double) groupedCategories.get(category).size() / totalCount * 100))
                ).sorted(Comparator.comparing(DistributionDto::getId))
                .toList();
    }

    public MonthlyExpenseMetricDto getMonthlyExpenseMetric() {
        var now = LocalDate.now();

        var startDate = Timestamp.valueOf(now.minusMonths(11).withDayOfMonth(1).atStartOfDay());
        var endDate = Timestamp.valueOf(now.withDayOfMonth(now.lengthOfMonth()).atTime(23, 59, 59));

        Specification<Expense> spec = Specification.where(null);
        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdDate"), startDate, endDate));

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("createdBy"), currentUserProvider.getCurrentUser()));

        var sort = Sort.by(Sort.Order.asc("createdDate"));

        var expenses = expenseRepository.findAll(spec, sort);

        Map<String, BigDecimal> monthlyExpenseMap = expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCreatedDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                        Collectors.mapping(Expense::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        List<String> months = new ArrayList<>(12);
        List<BigDecimal> amounts = new ArrayList<>(12);

        var currentDate = startDate.toLocalDateTime().toLocalDate();
        for (int i = 0; i < 12; i++) {
            String monthYear = currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
            months.add(monthYear);
            amounts.add(monthlyExpenseMap.getOrDefault(monthYear, BigDecimal.ZERO));

            currentDate = currentDate.plusMonths(1);
        }

        return new MonthlyExpenseMetricDto(months, amounts);
    }

    public BigDecimal getTotalExpenseAmount (TotalExpenseAmountRequestDto request) {
        var providedMonth = request.getMonth().toLocalDateTime().toLocalDate();

        var startOfMonth = Timestamp.valueOf(providedMonth.withDayOfMonth(1).atStartOfDay());
        var endOfMonth = Timestamp.valueOf(providedMonth.withDayOfMonth(providedMonth.lengthOfMonth()).atTime(23, 59, 59));

        Specification<Expense> spec = Specification.where(null);
        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdDate"), startOfMonth, endOfMonth));

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("createdBy"), currentUserProvider.getCurrentUser()));

        var expenses = expenseRepository.findAll(spec, Sort.unsorted());

        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public OperationResult reassignExpensesToNewCategory(ReassignExpenseCategoryRequestDto request) {
        Specification<Expense> spec = Specification.where(null);

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("createdBy"), currentUserProvider.getCurrentUser()));

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), request.getOldCategoryId()));

        var expenses = expenseRepository.findAll(spec, Sort.unsorted());

        if (expenses.isEmpty()) {
            return new OperationResult(false, "No expenses in selected category.");
        }

        for(Expense expense : expenses) {
            expense.setCategory(categoryService.getCategoryById(request.getNewCategoryId()));
            expenseRepository.save(expense);
        }

        return new OperationResult(true, expenses.size() + " expenses successfully updated.");
    }

    private boolean doesCurrentUserNotOwnExpense(Expense expense) {
        var currentUser = currentUserProvider.getCurrentUser();
        return !Objects.equals(expense.getCreatedBy().getId(), currentUser.getId()) &&
                !UserRole.ADMIN.equals(currentUser.getRole());
    }
}
