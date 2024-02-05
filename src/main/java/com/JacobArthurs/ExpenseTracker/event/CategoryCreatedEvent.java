package com.JacobArthurs.ExpenseTracker.event;

import com.JacobArthurs.ExpenseTracker.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryCreatedEvent {
    private final Category category;
}
