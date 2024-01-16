package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {
    public User getCurrentUser() {
        var currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return currentUser != null
                ? (User) currentUser
                : null;
    }
}
