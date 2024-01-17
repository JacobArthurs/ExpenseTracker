package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.DistributionDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpectedCategoryDistributionRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpectedCategoryDistributionSearchRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.PaginatedResponse;
import com.JacobArthurs.ExpenseTracker.enumerator.UserRole;
import com.JacobArthurs.ExpenseTracker.model.ExpectedCategoryDistribution;
import com.JacobArthurs.ExpenseTracker.repository.ExpectedCategoryDistributionRepository;
import com.JacobArthurs.ExpenseTracker.util.ExpectedCategoryDistributionUtil;
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

@Service
public class ExpectedCategoryDistributionService {
    private final ExpectedCategoryDistributionRepository expectedCategoryDistributionRepository;
    private final CategoryService categoryService;
    public final CurrentUserProvider currentUserProvider;

    @Autowired
    public ExpectedCategoryDistributionService(ExpectedCategoryDistributionRepository expectedCategoryDistribution, CategoryService categoryService, CurrentUserProvider currentUserProvider) {
        this.expectedCategoryDistributionRepository = expectedCategoryDistribution;
        this.categoryService = categoryService;
        this.currentUserProvider = currentUserProvider;
    }

    public List<ExpectedCategoryDistribution> getAllExpectedCategoryDistributions() {
        return expectedCategoryDistributionRepository.findAll();
    }

    public DistributionDto getAllDistributions() {
        var sort = Sort.by(Sort.Order.asc("id"));

        var expectedCategoryDistributions = expectedCategoryDistributionRepository.findAllByCreatedBy(currentUserProvider.getCurrentUser(), sort);

        var categories = expectedCategoryDistributions.stream()
                .map(distribution -> distribution.getCategory().getTitle())
                .collect(Collectors.toList());

        var distributions = expectedCategoryDistributions.stream()
                .map(ExpectedCategoryDistribution::getDistribution)
                .collect(Collectors.toList());

        return new DistributionDto(categories, distributions);
    }

    public ExpectedCategoryDistribution getExpectedCategoryDistributionById(Long id) {
        var expectedCategoryDistribution = expectedCategoryDistributionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expected category distribution not found with ID: " + id));

        if (doesCurrentUserNotOwnExpectedCategoryDistribution(expectedCategoryDistribution))
            throw new RuntimeException("You are not authorized to get an expected category distribution that is not yours.");
        else
            return expectedCategoryDistribution;
    }

    public ExpectedCategoryDistribution createExpectedCategoryDistribution(ExpectedCategoryDistributionRequestDto request) {
        var expectedCategoryDistribution = ExpectedCategoryDistributionUtil.convertRequestToObject(request, categoryService);
        expectedCategoryDistribution.setCreatedBy(currentUserProvider.getCurrentUser());
        expectedCategoryDistribution.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        return expectedCategoryDistributionRepository.save(expectedCategoryDistribution);
    }

    public ExpectedCategoryDistribution updateExpectedCategoryDistribution(Long id, ExpectedCategoryDistributionRequestDto request) {
        var expectedCategoryDistribution = expectedCategoryDistributionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expected category distribution not found with ID: " + id));

        expectedCategoryDistribution.setDistribution(request.getDistribution());
        expectedCategoryDistribution.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        if (doesCurrentUserNotOwnExpectedCategoryDistribution(expectedCategoryDistribution))
            throw new RuntimeException("You are not authorized to get an expected category distribution that is not yours.");
        else
            return expectedCategoryDistributionRepository.save(expectedCategoryDistribution);
    }

    public boolean deleteExpectedCategoryDistribution(Long id) {
        var expectedCategoryDistribution = expectedCategoryDistributionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expected category distribution not found with ID: " + id));

        if (doesCurrentUserNotOwnExpectedCategoryDistribution(expectedCategoryDistribution))
            throw new RuntimeException("You are not authorized to get an expected category distribution that is not yours.");
        else {
            expectedCategoryDistributionRepository.deleteById(id);
            return true;
        }
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

    private boolean doesCurrentUserNotOwnExpectedCategoryDistribution(ExpectedCategoryDistribution expectedCategoryDistribution) {
        var currentUser = currentUserProvider.getCurrentUser();
        return !Objects.equals(expectedCategoryDistribution.getCreatedBy().getId(), currentUser.getId()) &&
                !UserRole.ADMIN.equals(currentUser.getRole());
    }
}
