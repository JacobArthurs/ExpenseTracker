package com.JacobArthurs.ExpenseTracker.dto;

import com.JacobArthurs.ExpenseTracker.model.Category;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class CategoryDto {
    private Long id;
    private String title;
    private String description;
    private Timestamp createdDate;
    private Timestamp lastUpdatedDate;
    private Long expectedCategoryDistributionId;

    public CategoryDto(Category category){
        this.id = category.getId();
        this.title = category.getTitle();
        this.description = category.getDescription();
        this.createdDate = category.getCreatedDate();
        this.lastUpdatedDate = category.getLastUpdatedDate();
        this.expectedCategoryDistributionId = category.getExpectedCategoryDistribution().getId();
    }
}
