package com.JacobArthurs.ExpenseTracker.util;

import com.JacobArthurs.ExpenseTracker.dto.CategoryDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpectedCategoryDistributionDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpectedCategoryDistributionRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.PaginatedResponse;
import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.model.ExpectedCategoryDistribution;
import com.JacobArthurs.ExpenseTracker.service.CategoryService;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class ExpectedCategoryDistributionUtil {
    public static PaginatedResponse<ExpectedCategoryDistributionDto> convertPaginatedToPaginatedDto(PaginatedResponse<ExpectedCategoryDistribution> paginatedExpectedCategoryDistributions) {
        List<ExpectedCategoryDistributionDto> expectedCategoryDistributionDtos = paginatedExpectedCategoryDistributions.getData().stream()
                .map(ExpectedCategoryDistributionDto::new)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(paginatedExpectedCategoryDistributions.getLimit(), paginatedExpectedCategoryDistributions.getOffset(), paginatedExpectedCategoryDistributions.getTotal(), expectedCategoryDistributionDtos);
    }

    public static ExpectedCategoryDistribution convertRequestToObject(ExpectedCategoryDistributionRequestDto request, CategoryService categoryService) {
        var expectedCategoryDistribution = new ExpectedCategoryDistribution();
        expectedCategoryDistribution.setDistribution(request.getDistribution());
        expectedCategoryDistribution.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        var category = categoryService.getCategoryById(request.getCategoryId());
        expectedCategoryDistribution.setCategory(category);

        return expectedCategoryDistribution;
    }
}
