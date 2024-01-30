package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.CategoryRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.CategorySearchRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.OperationResult;
import com.JacobArthurs.ExpenseTracker.dto.PaginatedResponse;
import com.JacobArthurs.ExpenseTracker.enumerator.UserRole;
import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.model.User;
import com.JacobArthurs.ExpenseTracker.repository.CategoryRepository;
import com.JacobArthurs.ExpenseTracker.util.CategoryUtil;
import com.JacobArthurs.ExpenseTracker.util.OffsetBasedPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public final CurrentUserProvider currentUserProvider;

    public CategoryService(CategoryRepository categoryRepository, CurrentUserProvider currentUserProvider) {
        this.categoryRepository = categoryRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public List<Category> getAllCategories() {
        var sort = Sort.by(
                Sort.Order.desc("createdDate"),
                Sort.Order.desc("lastUpdatedDate"),
                Sort.Order.asc("id"));

        return categoryRepository.findAllByCreatedBy(currentUserProvider.getCurrentUser(), sort);
    }

    public Category getCategoryById(Long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        if (doesCurrentUserNotOwnCategory(category))
            throw new RuntimeException("You are not authorized to get a category that is not yours");
        else
            return category;
    }

    public OperationResult createCategory(CategoryRequestDto request) {
        var categoryCount = categoryRepository.countByCreatedBy(currentUserProvider.getCurrentUser());
        if (categoryCount >= 10) {
            return new OperationResult(false, "Maximum 10 categories allowed. Please remove some before adding more.");
        }

        var category = CategoryUtil.convertRequestToObject(request);

        category.setCreatedBy(currentUserProvider.getCurrentUser());
        category.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        categoryRepository.save(category);

        return new OperationResult(true, "Category created successfully");
    }

    public OperationResult updateCategory(Long id, CategoryRequestDto request) {
        var category = categoryRepository.findById(id).orElse(null);

        if (category == null)
            return new OperationResult(false, "Category not found with ID: " + id);
        if (doesCurrentUserNotOwnCategory(category))
            return new OperationResult(false, "You are not authorized to update a category that is not yours");

        category.setTitle(request.getTitle());
        category.setDescription(request.getDescription());
        category.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        categoryRepository.save(category);

        return new OperationResult(true, "Category updated successfully");
    }

    public OperationResult deleteCategory(Long id) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null)
            return new OperationResult(false, "Category not found with ID: " + id);
        if (doesCurrentUserNotOwnCategory(category))
            return new OperationResult(false, "You are not authorized to delete a category that is not yours");

        categoryRepository.deleteById(id);
        return new OperationResult(true, "Category deleted successfully");
    }

    public PaginatedResponse<Category> searchCategories(CategorySearchRequestDto request) {
        Specification<Category> spec = Specification.where(null);

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("createdBy"), currentUserProvider.getCurrentUser()));

        if (request.getId() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("id"), request.getId()));
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

        Page<Category> categoryPage = categoryRepository.findAll(spec, pageable);

        return new PaginatedResponse<>(request.getLimit(), request.getOffset(), categoryPage.getTotalElements(), categoryPage.getContent());
    }

    public List<Category> createSeedData(User user) {
        var currentTime = new Timestamp(System.currentTimeMillis());

        var categories = Arrays.asList(
                new Category("Housing", "Expenses related to housing.", currentTime, currentTime, user),
                new Category("Transportation", "Costs associated with transportation.", currentTime, currentTime, user),
                new Category("Food", "Expenditures on food.", currentTime, currentTime, user),
                new Category("Utilities", "Costs for utilities.", currentTime, currentTime, user),
                new Category("Insurance", "Expenditures for various types of insurance coverage.", currentTime, currentTime, user),
                new Category("Medical & Healthcare", "Expenses related to medical and healthcare services.", currentTime, currentTime, user),
                new Category("Saving, Investing, & Debt Payments", "Allocations for saving, investing, and debt payments.", currentTime, currentTime, user),
                new Category("Personal Spending", "Personal discretionary spending.", currentTime, currentTime, user),
                new Category("Recreation & Entertainment", "Costs associated with recreation and entertainment.", currentTime, currentTime, user),
                new Category("Miscellaneous", "Miscellaneous expenses.", currentTime, currentTime, user)
        );

        return categoryRepository.saveAll(categories);
    }

    private boolean doesCurrentUserNotOwnCategory(Category category) {
        var currentUser = currentUserProvider.getCurrentUser();
        return !Objects.equals(category.getCreatedBy().getId(), currentUser.getId()) &&
                !UserRole.ADMIN.equals(currentUser.getRole());
    }
}
