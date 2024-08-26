package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriend(Long id, Long friendId) {
        userNotNullValidate(id);
        userNotNullValidate(friendId);
        User user1 = userStorage.getUser(id);
        User user2 = userStorage.getUser(friendId);
        if (user1.getFriends().contains(friendId)) {
            throw new ValidationException("Друг с таким id уже добавлен");
        }
        user1.getFriends().add(friendId);

        if (user2.getFriends().contains(id)) {
            throw new ValidationException("Друг с таким id уже добавлен");
        }
        user2.getFriends().add(id);
    }

    public void deleteFriend(Long id, Long friendId) {
        userNotNullValidate(id);
        userNotNullValidate(friendId);
        User user1 = userStorage.getUser(id);
        User user2 = userStorage.getUser(friendId);
        user1.getFriends().remove(friendId);
        user2.getFriends().remove(id);
    }

    public List<User> getFriendsUser(Long id) {
        userNotNullValidate(id);
        return userStorage.getUser(id).getFriends().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        final User user = userStorage.getUserById(id);
        final User otherUser = userStorage.getUserById(otherId);

        return otherUser.getFriends().stream()
                .filter(userId -> user.getFriends().contains(userId))
                .map(userStorage::getUserById)
                .toList();
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public void delete(Long userId) {
        userStorage.delete(userId);
    }

    private void userNotNullValidate(long userId) {
        if (userStorage.getUser(userId) == null) {
            throw new ValidationException("Пользователь с таким id не найден");
        }
    }
}
