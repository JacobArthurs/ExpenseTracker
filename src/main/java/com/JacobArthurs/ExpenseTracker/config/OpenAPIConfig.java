package com.JacobArthurs.ExpenseTracker.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Expense Tracker API", description = "A simple CRUD RESTful API to track expenses"))
public class OpenAPIConfig {
}