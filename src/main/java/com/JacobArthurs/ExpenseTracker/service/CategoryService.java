package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.CategoryRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.CategorySearchRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.PaginatedResponse;
import com.JacobArthurs.ExpenseTracker.enumerator.UserRole;
import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.repository.CategoryRepository;
import com.JacobArthurs.ExpenseTracker.util.CategoryUtil;
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

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public final CurrentUserProvider currentUserProvider;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CurrentUserProvider currentUserProvider) {
        this.categoryRepository = categoryRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));;

        if (doesCurrentUserNotOwnCategory(category))
            throw new RuntimeException("You are not authorized to get a category that is not yours.");
        else
            return category;
    }

    public Category createCategory(CategoryRequestDto request) {
        var category = CategoryUtil.convertRequestToObject(request);

        category.setCreatedBy(currentUserProvider.getCurrentUser());
        category.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, CategoryRequestDto request) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));;

        if (doesCurrentUserNotOwnCategory(category))
            throw new RuntimeException("You are not authorized to update a category that is not yours.");

        category.setTitle(request.getTitle());
        category.setDescription(request.getDescription());
        category.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        return categoryRepository.save(category);
    }

    public boolean deleteCategory(Long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
        if (doesCurrentUserNotOwnCategory(category))
            throw new RuntimeException("You are not authorized to delete a category that is not yours.");

        categoryRepository.deleteById(id);
        return true;
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

    private boolean doesCurrentUserNotOwnCategory(Category category) {
        var currentUser = currentUserProvider.getCurrentUser();
        return !Objects.equals(category.getCreatedBy().getId(), currentUser.getId()) &&
                !UserRole.ADMIN.equals(currentUser.getRole());
    }
}
