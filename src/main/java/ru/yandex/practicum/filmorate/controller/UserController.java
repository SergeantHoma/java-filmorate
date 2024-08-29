package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        final Collection<User> users = userService.getAllUsers();
        log.info("Запрос на всех пользователей");
        return users;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable(value = "id") Long id,
                          @PathVariable(value = "friendId") Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable(value = "id") Long id,
                             @PathVariable(value = "friendId") Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsUser(@PathVariable(value = "id") Long id) {
        final Collection<User> friendsUser = userService.getFriendsUser(id);
        log.info("Запрос на всех друзей пользователя с id = {}", id);
        return friendsUser;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable(value = "id") Long id,
                                             @PathVariable(value = "otherId") Long otherId) {
        final Collection<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("Запрос на общих друзей пользователей с id = {}, {}", id, otherId);
        return commonFriends;
    }
}
