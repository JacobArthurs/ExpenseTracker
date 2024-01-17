package com.JacobArthurs.ExpenseTracker.util;

import com.JacobArthurs.ExpenseTracker.dto.OperationResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtil {
    public static <T> OperationResult validate(T object) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

        try (validatorFactory) {
            Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<T>> violations = validator.validate(object);

            if (!violations.isEmpty()) {
                String validationMessages = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", "));

                return new OperationResult(false, validationMessages);
            }

            return new OperationResult(true, null);
        }
    }
}
