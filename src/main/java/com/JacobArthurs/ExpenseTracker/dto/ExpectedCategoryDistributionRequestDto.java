package com.JacobArthurs.ExpenseTracker.dto;

import lombok.Getter;

@Getter
public class ExpectedCategoryDistributionRequestDto {
    private Long categoryId;
    private int minimumDistribution;
    private int maximumDistribution;
}
