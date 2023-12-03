package com.JacobArthurs.ExpenseTracker.dto;

import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.model.ExpectedCategoryDistribution;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class ExpectedCategoryDistributionDto {
    private Long id;
    private Long categoryId;
    private int minimumDistribution;
    private int maximumDistribution;
    private Timestamp createdDate;
    private Timestamp lastUpdatedDate;

    public ExpectedCategoryDistributionDto(ExpectedCategoryDistribution expectedCategoryDistribution){
        this.id = expectedCategoryDistribution.getId();
        this.categoryId = expectedCategoryDistribution.getCategory().getId();
        this.minimumDistribution = expectedCategoryDistribution.getMinimumDistribution();
        this.maximumDistribution = expectedCategoryDistribution.getMaximumDistribution();
        this.createdDate = expectedCategoryDistribution.getCreatedDate();
        this.lastUpdatedDate = expectedCategoryDistribution.getLastUpdatedDate();
    }
}
