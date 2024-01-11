package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.CategorySearchRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpectedCategoryDistributionRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpectedCategoryDistributionSearchRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.PaginatedResponse;
import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.model.ExpectedCategoryDistribution;
import com.JacobArthurs.ExpenseTracker.repository.ExpectedCategoryDistributionRepository;
import com.JacobArthurs.ExpenseTracker.util.ExpectedCategoryDistributionUtil;
import com.JacobArthurs.ExpenseTracker.util.OffsetBasedPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ExpectedCategoryDistributionService {
    private final ExpectedCategoryDistributionRepository expectedCategoryDistributionRepository;
    private final CategoryService categoryService;

    @Autowired
    public ExpectedCategoryDistributionService(ExpectedCategoryDistributionRepository expectedCategoryDistribution, CategoryService categoryService) {
        this.expectedCategoryDistributionRepository = expectedCategoryDistribution;
        this.categoryService = categoryService;
    }

    public List<ExpectedCategoryDistribution> getAllCategories() {
        return expectedCategoryDistributionRepository.findAll();
    }

    public ExpectedCategoryDistribution getCategoryById(Long  id) {
        return expectedCategoryDistributionRepository.findById(id).orElse(null);
    }

    public ExpectedCategoryDistribution createCategory(ExpectedCategoryDistributionRequestDto request) {
        var expectedCategoryDistribution = ExpectedCategoryDistributionUtil.convertRequestToObject(request, categoryService);
        expectedCategoryDistribution.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        return expectedCategoryDistributionRepository.save(expectedCategoryDistribution);
    }

    public ExpectedCategoryDistribution updateCategory(Long id, ExpectedCategoryDistributionRequestDto request) {
        if (expectedCategoryDistributionRepository.existsById(id)) {
            var expectedCategoryDistribution = ExpectedCategoryDistributionUtil.convertRequestToObject(request, categoryService);
            expectedCategoryDistribution.setId(id);

            return expectedCategoryDistributionRepository.save(expectedCategoryDistribution);
        } else {
            return null;
        }
    }

    public boolean deleteCategory(Long id) {
        if (expectedCategoryDistributionRepository.existsById(id)) {
            expectedCategoryDistributionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public PaginatedResponse<ExpectedCategoryDistribution> searchExpectedCategoryDistributions(ExpectedCategoryDistributionSearchRequestDto request) {
        Specification<ExpectedCategoryDistribution> spec = Specification.where(null);

        if (request.getId() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("id"), request.getId()));
        }

        if (request.getCategoryId() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("categoryId"), request.getCategoryId()));
        }

        if (request.getCreatedDate() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("createdDate"), request.getCreatedDate()));
        }

        if (request.getLastUpdatedDate() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("lastUpdatedDate"), request.getLastUpdatedDate()));
        }

        Pageable pageable = new OffsetBasedPageRequest(request.getOffset(), request.getLimit());

        Page<ExpectedCategoryDistribution> categoryPage = expectedCategoryDistributionRepository.findAll(spec, pageable);

        return new PaginatedResponse<>(request.getLimit(), request.getOffset(), categoryPage.getTotalElements(), categoryPage.getContent());
    }
}
