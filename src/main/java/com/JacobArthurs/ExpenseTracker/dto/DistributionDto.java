package com.JacobArthurs.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DistributionDto {
    private Long id;
    private String category;
    private int distribution;
}
