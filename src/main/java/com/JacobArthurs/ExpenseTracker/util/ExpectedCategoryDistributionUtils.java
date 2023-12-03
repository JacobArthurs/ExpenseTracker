package com.JacobArthurs.ExpenseTracker.util;

import com.JacobArthurs.ExpenseTracker.dto.ExpectedCategoryDistributionDto;
import com.JacobArthurs.ExpenseTracker.dto.ExpectedCategoryDistributionRequestDto;
import com.JacobArthurs.ExpenseTracker.model.ExpectedCategoryDistribution;
import com.JacobArthurs.ExpenseTracker.service.CategoryService;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class ExpectedCategoryDistributionUtils {
    public static List<ExpectedCategoryDistributionDto> convertObjectListToDtoList(List<ExpectedCategoryDistribution> expectedCategoryDistributions) {
        return expectedCategoryDistributions.stream()
                .map(ExpectedCategoryDistributionDto::new)
                .collect(Collectors.toList());
    }

    public static ExpectedCategoryDistribution convertRequestToObject(ExpectedCategoryDistributionRequestDto request, CategoryService categoryService) {
        var expectedCategoryDistribution = new ExpectedCategoryDistribution();
        expectedCategoryDistribution.setMinimumDistribution(request.getMinimumDistribution());
        expectedCategoryDistribution.setMaximumDistribution(request.getMaximumDistribution());
        expectedCategoryDistribution.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));

        var category = categoryService.getCategoryById(request.getCategoryId());
        expectedCategoryDistribution.setCategory(category);

        return expectedCategoryDistribution;
    }
}
