package com.JacobArthurs.ExpenseTracker.event;

import com.JacobArthurs.ExpenseTracker.dto.ExpectedCategoryDistributionRequestDto;
import com.JacobArthurs.ExpenseTracker.service.ExpectedCategoryDistributionService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CategoryCreatedEventHandler {
    private final ExpectedCategoryDistributionService expectedCategoryDistributionService;

    public CategoryCreatedEventHandler(ExpectedCategoryDistributionService expectedCategoryDistributionService){
        this.expectedCategoryDistributionService = expectedCategoryDistributionService;
    }
    @EventListener
    public void onCategoryCreated(CategoryCreatedEvent event) {
        expectedCategoryDistributionService.createExpectedCategoryDistribution(new ExpectedCategoryDistributionRequestDto(event.getCategory().getId(), 0));
    }
}
