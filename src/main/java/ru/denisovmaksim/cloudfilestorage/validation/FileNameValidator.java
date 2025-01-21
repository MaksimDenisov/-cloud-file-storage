package ru.denisovmaksim.cloudfilestorage.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNameValidator implements ConstraintValidator<ValidFileName, String> {
    private Pattern pattern;

    @Override
    public void initialize(ValidFileName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        String regexp = "^[^/\\\\:*?\"<>|]+$";
        pattern = Pattern.compile(regexp);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
