package com.JacobArthurs.ExpenseTracker.dto;

import com.JacobArthurs.ExpenseTracker.model.Expense;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
public class ExpenseDto {
    private Long id;
    private String category;
    private String title;
    private String description;
    private BigDecimal amount;
    private Timestamp createdDate;
    private Timestamp lastUpdatedDate;

    public ExpenseDto(Expense expense){
        this.id = expense.getId();
        this.category = expense.getCategory().getTitle();
        this.title = expense.getTitle();
        this.description = expense.getDescription();
        this.amount = expense.getAmount();
        this.createdDate = expense.getCreatedDate();
        this.lastUpdatedDate = expense.getLastUpdatedDate();
    }
}
