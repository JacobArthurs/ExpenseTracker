package com.JacobArthurs.ExpenseTracker.dto;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class ExpectedCategoryDistributionSearchRequestDto extends PaginatedRequest {
    private Long id;
    private Long categoryId;
    private Timestamp startDate;
    private Timestamp endDate;
}
