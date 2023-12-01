package com.JacobArthurs.ExpenseTracker.dto;

import com.JacobArthurs.ExpenseTracker.model.Expense;
import lombok.Getter;
import java.sql.Timestamp;

@Getter
public class ExpenseDto {
    private Long id;
    private Long categoryId;
    private String title;
    private String description;
    private Timestamp createdDate;
    private Timestamp lastUpdatedDate;

    public ExpenseDto(Expense expense){
        this.id = expense.getId();
        this.categoryId = expense.getCategory().getId();
        this.title = expense.getTitle();
        this.description = expense.getDescription();
        this.createdDate = expense.getCreatedDate();
        this.lastUpdatedDate = expense.getLastUpdatedDate();
    }
}
