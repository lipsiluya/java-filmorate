package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserValidator;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User addUser(User user) {
        UserValidator.validate(user);
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        UserValidator.validate(user);
        return userStorage.update(user);
    }

    public User getUser(Long id) {
        return userStorage.getById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        // добавляем дружбу с подтверждённым статусом
        userStorage.addFriend(userId, friendId, FriendshipStatus.CONFIRMED);
    }

    public void removeFriend(Long userId, Long friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public Set<User> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    public Set<User> getCommonFriends(Long userId, Long otherId) {
        return userStorage.getCommonFriends(userId, otherId);
    }
}