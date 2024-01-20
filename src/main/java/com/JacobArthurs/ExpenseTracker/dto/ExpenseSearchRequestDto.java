package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
public class ExpenseSearchRequestDto extends PaginatedRequest {
    private Long categoryId;

    @Size(max = 50)
    private String title;

    @Size(max = 200)
    private String description;

    @Size(max = 200)
    private String overviewText;

    private Timestamp startDate;

    private Timestamp endDate;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;
}
