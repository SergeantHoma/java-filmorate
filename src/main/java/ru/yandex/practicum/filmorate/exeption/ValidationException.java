package ru.yandex.practicum.filmorate.exeption;

public class ValidationException extends RuntimeException {
    ValidationException(final String message) {
        super(message);
    }
}
