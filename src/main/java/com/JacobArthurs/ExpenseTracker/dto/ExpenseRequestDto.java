package com.JacobArthurs.ExpenseTracker.dto;

import lombok.Getter;

@Getter
public class ExpenseRequestDto {
    private Long categoryId;
    private String title;
    private String description;
}
