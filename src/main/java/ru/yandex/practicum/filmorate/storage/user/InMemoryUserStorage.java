package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long,User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id: %d не найден", userId)));
    }

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(@Valid @RequestBody User user) {
        userValidation(user);
        user.setId(getId());
        users.put(user.getId(), user);
        log.info("Запрос на создание пользователя. Пользователь добавлен.");
        return user;
    }

    @Override
    public User update(User user) {
        if (users.get(user.getId()) != null) {
            userValidation(user);
            users.put(user.getId(), user);
            log.info("Запрос на изменение пользователя. Пользователь изменён.");
        } else {
            log.warn("Запрос на изменение пользователя. Пользователь не найден");
            throw new NotFoundException("Пользователь не найден.");
        }
        return user;
    }

    @Override
    public void delete(Long userId) {
        if (!users.containsKey(userId)) {
            log.info("Запрос на удаление пользователя. Пользователь не найден");
            throw new NotFoundException("Пользователь не найден");
        } else {
            users.remove(userId);
            log.info("Запрос на удаление пользователя. Пользователь удален");
        }
    }

    @Override
    public User getUser(Long userId) {
        return users.get(userId);
    }

    private Long getId() {
        id++;
        return id;
    }

    private void userValidation(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
