package com.JacobArthurs.ExpenseTracker.controller;

import com.JacobArthurs.ExpenseTracker.dto.OperationResult;
import com.JacobArthurs.ExpenseTracker.dto.UserLoginRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.UserRegisterRequestDto;
import com.JacobArthurs.ExpenseTracker.service.UserService;
import com.JacobArthurs.ExpenseTracker.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@Tag(name = "Authentication controller", description = "Authentication endpoints.")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login with credentials", description = "Returns a JWT token")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginRequestDto request) {
        var isValidLogin = userService.validateLogin(request);

        if (isValidLogin)
            return ResponseEntity.ok(JwtTokenUtil.generateToken(request.getUserName()));

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user")
    public ResponseEntity<OperationResult> register(@RequestBody @Valid UserRegisterRequestDto request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }
}
