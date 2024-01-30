package com.JacobArthurs.ExpenseTracker.event;

import com.JacobArthurs.ExpenseTracker.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreatedEvent {
    private final User user;
}
