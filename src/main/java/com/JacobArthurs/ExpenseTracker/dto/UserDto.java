package com.JacobArthurs.ExpenseTracker.dto;

import com.JacobArthurs.ExpenseTracker.model.User;
import lombok.Getter;

@Getter
public class UserDto {
    private long id;
    private String username;
    private String name;
    private String email;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
