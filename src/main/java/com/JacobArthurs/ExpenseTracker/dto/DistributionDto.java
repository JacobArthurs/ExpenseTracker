package com.JacobArthurs.ExpenseTracker.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DistributionDto {
    private List<String> categories;
    private List<Integer> distributions;

    public DistributionDto(List<String> categories, List<Integer> distributions) {
        this.categories = categories;
        this.distributions = distributions;
    }
}
