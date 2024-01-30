package com.JacobArthurs.ExpenseTracker.event;

import com.JacobArthurs.ExpenseTracker.service.CategoryService;
import com.JacobArthurs.ExpenseTracker.service.ExpectedCategoryDistributionService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventHandler {
    private final CategoryService categoryService;
    private final ExpectedCategoryDistributionService expectedCategoryDistributionService;

    public UserCreatedEventHandler(CategoryService categoryService, ExpectedCategoryDistributionService expectedCategoryDistributionService){
        this.categoryService = categoryService;
        this.expectedCategoryDistributionService = expectedCategoryDistributionService;
    }
    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        var categories = categoryService.createSeedData(event.getUser());
        expectedCategoryDistributionService.createSeedData(categories, event.getUser());
    }
}
