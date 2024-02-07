package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.*;
import com.JacobArthurs.ExpenseTracker.enumerator.UserRole;
import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.model.ExpectedCategoryDistribution;
import com.JacobArthurs.ExpenseTracker.model.User;
import com.JacobArthurs.ExpenseTracker.repository.ExpectedCategoryDistributionRepository;
import com.JacobArthurs.ExpenseTracker.util.ExpectedCategoryDistributionUtil;
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
import java.util.stream.Collectors;

@Service
public class ExpectedCategoryDistributionService {
    private final ExpectedCategoryDistributionRepository expectedCategoryDistributionRepository;
    private final CategoryService categoryService;
    public final CurrentUserProvider currentUserProvider;

    public ExpectedCategoryDistributionService(ExpectedCategoryDistributionRepository expectedCategoryDistribution, CategoryService categoryService, CurrentUserProvider currentUserProvider) {
        this.expectedCategoryDistributionRepository = expectedCategoryDistribution;
        this.categoryService = categoryService;
        this.currentUserProvider = currentUserProvider;
    }

    public List<ExpectedCategoryDistribution> getAllExpectedCategoryDistributions() {
        return expectedCategoryDistributionRepository.findAll();
    }

    public List<DistributionDto> getAllDistributions() {
        var sort = Sort.by(Sort.Order.asc("id"));

        var expectedCategoryDistributions = expectedCategoryDistributionRepository.findAllByCreatedBy(currentUserProvider.getCurrentUser(), sort);

        return expectedCategoryDistributions.stream()
                .map(dist -> new DistributionDto(dist.getId(), dist.getCategory().getTitle(), dist.getCategory().getId(), dist.getDistribution()))
                .collect(Collectors.toList());
    }

    public ExpectedCategoryDistribution getExpectedCategoryDistributionById(Long id) {
        var expectedCategoryDistribution = expectedCategoryDistributionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expected category distribution not found with ID: " + id));

        if (doesCurrentUserNotOwnExpectedCategoryDistribution(expectedCategoryDistribution))
            throw new RuntimeException("You are not authorized to get an expected category distribution that is not yours.");
        else
            return expectedCategoryDistribution;
    }

    public OperationResult createExpectedCategoryDistribution(ExpectedCategoryDistributionRequestDto request) {
        var expectedCategoryDistribution = ExpectedCategoryDistributionUtil.convertRequestToObject(request, categoryService);
        expectedCategoryDistribution.setCreatedBy(currentUserProvider.getCurrentUser());
        expectedCategoryDistribution.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        expectedCategoryDistributionRepository.save(expectedCategoryDistribution);
        return new OperationResult(true, "Expected category distribution created successfully");
    }

    public OperationResult updateExpectedCategoryDistribution(Long id, ExpectedCategoryDistributionRequestDto request) {
        var expectedCategoryDistribution = expectedCategoryDistributionRepository.findById(id).orElse(null);

        if (expectedCategoryDistribution == null)
            return new OperationResult(false, "Expected category distribution not found with ID: " + id);
        if (doesCurrentUserNotOwnExpectedCategoryDistribution(expectedCategoryDistribution))
            return new OperationResult(false, "You are not authorized to update an expected category distribution that is not yours.");

        expectedCategoryDistribution.setDistribution(request.getDistribution());
        expectedCategoryDistribution.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        expectedCategoryDistributionRepository.save(expectedCategoryDistribution);
        return new OperationResult(true, "Expected category distribution updated successfully");
    }

    public OperationResult deleteExpectedCategoryDistribution(Long id) {
        var expectedCategoryDistribution = expectedCategoryDistributionRepository.findById(id).orElse(null);

        if (expectedCategoryDistribution == null)
            return new OperationResult(false, "Expected category distribution not found with ID: " + id);
        if (doesCurrentUserNotOwnExpectedCategoryDistribution(expectedCategoryDistribution))
            return new OperationResult(false, "You are not authorized to delete an expected category distribution that is not yours.");

        expectedCategoryDistributionRepository.deleteById(id);
        return new OperationResult(true, "Expected category distribution deleted successfully");
    }

    public PaginatedResponse<ExpectedCategoryDistribution> searchExpectedCategoryDistributions(ExpectedCategoryDistributionSearchRequestDto request) {
        Specification<ExpectedCategoryDistribution> spec = Specification.where(null);

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

        Page<ExpectedCategoryDistribution> categoryPage = expectedCategoryDistributionRepository.findAll(spec, pageable);

        return new PaginatedResponse<>(request.getLimit(), request.getOffset(), categoryPage.getTotalElements(), categoryPage.getContent());
    }

    public void createSeedData(List<Category> categories, User user) {
        var currentTime = new Timestamp(System.currentTimeMillis());

        var expectedCategoryDistributions = Arrays.asList(
                new ExpectedCategoryDistribution(categories.get(0), 25, currentTime, currentTime, user),  // Housing
                new ExpectedCategoryDistribution(categories.get(1), 15, currentTime, currentTime, user),  // Transportation
                new ExpectedCategoryDistribution(categories.get(2), 15, currentTime, currentTime, user),  // Food
                new ExpectedCategoryDistribution(categories.get(3), 10, currentTime, currentTime, user),  // Utilities
                new ExpectedCategoryDistribution(categories.get(4), 10, currentTime, currentTime, user),  // Insurance
                new ExpectedCategoryDistribution(categories.get(5), 5, currentTime, currentTime, user),   // Medical & Healthcare
                new ExpectedCategoryDistribution(categories.get(6), 5, currentTime, currentTime, user),   // Saving, Investing, & Debt Payments
                new ExpectedCategoryDistribution(categories.get(7), 5, currentTime, currentTime, user),   // Personal Spending
                new ExpectedCategoryDistribution(categories.get(8), 5, currentTime, currentTime, user),   // Recreation & Entertainment
                new ExpectedCategoryDistribution(categories.get(9), 5, currentTime, currentTime, user)    // Miscellaneous
        );

        expectedCategoryDistributionRepository.saveAll(expectedCategoryDistributions);
    }

    private boolean doesCurrentUserNotOwnExpectedCategoryDistribution(ExpectedCategoryDistribution expectedCategoryDistribution) {
        var currentUser = currentUserProvider.getCurrentUser();
        return !Objects.equals(expectedCategoryDistribution.getCreatedBy().getId(), currentUser.getId()) &&
                !UserRole.ADMIN.equals(currentUser.getRole());
    }
}
