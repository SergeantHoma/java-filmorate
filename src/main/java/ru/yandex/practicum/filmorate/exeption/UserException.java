package ru.yandex.practicum.filmorate.exeption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserException extends RuntimeException {
    public UserException(String massage) {
        super(massage);
    }
}
