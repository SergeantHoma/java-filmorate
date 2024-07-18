package ru.yandex.practicum.filmorate.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IncludingSpaceValidator implements ConstraintValidator<IncludingSpace, String> {
    public void initialize(IncludingSpace parameters) {
        // Nothing to do here
    }

    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !value.contains(" ");
    }

}
