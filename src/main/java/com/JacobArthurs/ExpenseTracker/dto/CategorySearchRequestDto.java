package com.JacobArthurs.ExpenseTracker.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class CategorySearchRequestDto extends PaginatedRequest {
    private Long id;

    @Size(max = 50)
    private String title;

    @Size(max = 200)
    private String description;

    @Size(max = 200)
    private String overviewText;

    private Timestamp createdDate;

    private Timestamp lastUpdatedDate;
}
