package com.JacobArthurs.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DistributionDto {
    private List<String> categories;
    private List<Integer> distributions;
}
