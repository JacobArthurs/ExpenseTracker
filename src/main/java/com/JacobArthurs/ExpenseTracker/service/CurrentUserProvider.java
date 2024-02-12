package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {
    /**
     * Retrieves the currently authenticated user.
     *
     * @return The authenticated user, or null if no user is authenticated
     */
    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        } else {
            return null;
        }
    }
}
