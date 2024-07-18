package ru.yandex.practicum.filmorate.exeption;

public class UserException extends RuntimeException {
    public UserException(String massage) {
        super(massage);
    }
}
