package com.JacobArthurs.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OperationResult {
    private boolean success;
    private String message;
}
