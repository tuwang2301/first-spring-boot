package com.example.demo.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class EnumValueValidator implements ConstraintValidator<ValidEnumValue, Enum<?>> {
    private List<String> allowedValues;
    @Override
    public void initialize(ValidEnumValue constraintAnnotation) {
        allowedValues =  Arrays.asList(constraintAnnotation.allowedValues());
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true; // Let other annotations handle null value
        }

        return allowedValues.contains(value.toString());
    }
}
