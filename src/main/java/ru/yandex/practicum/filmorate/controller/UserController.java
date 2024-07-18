package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.UserException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer,User> usersMap = new HashMap<>();
    private int id = 0;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userValidation(user);
        user.setId(getId());
        usersMap.put(user.getId(), user);
        log.info("Запрос на создание пользователя. Пользователь добавлен.");
        return user;
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        if (usersMap.get(user.getId()) != null) {
            userValidation(user);
            usersMap.put(user.getId(), user);
            log.info("Запрос на изменение пользователя. Пользователь изменён.");
        } else {
            log.warn("Запрос на изменение пользователя. Пользователь не найден");
            throw new UserException("Пользователь не найден.");
        }
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(usersMap.values());
    }

    private int getId() {
        id++;
        return id;
    }

    private void userValidation(User user) throws ValidationException {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
