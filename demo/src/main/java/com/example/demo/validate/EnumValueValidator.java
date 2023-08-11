package com.example.demo.validate;

import com.example.demo.errorhandler.StudentErrors;
import com.example.demo.errorhandler.StudentException;
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

        if(!allowedValues.contains(value.toString())){
            throw new StudentException(StudentErrors.Gender_Invalid);
        }

        return true;
    }
}
