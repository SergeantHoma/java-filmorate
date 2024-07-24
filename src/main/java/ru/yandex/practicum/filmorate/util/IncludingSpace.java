package ru.yandex.practicum.filmorate.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = IncludingSpaceValidator.class)
public @interface IncludingSpace {
    String message() default "{javax.validation.constraints.NotBlank.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
