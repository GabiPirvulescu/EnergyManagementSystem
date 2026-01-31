package com.energymgmt.user_service.dtos.validators;

import com.energymgmt.user_service.dtos.validators.annotation.AgeLimit;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AgeValidator implements ConstraintValidator<AgeLimit, Integer> {
    private int min;
    @Override public void initialize(AgeLimit ann) { this.min = ann.value(); }
    @Override public boolean isValid(Integer age, ConstraintValidatorContext ctx) {
        if (age == null) return true;
        return age >= min;
    }
}