package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class UserService {

    private final InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User addUser(User user) {
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public User getUser(Long id) {
        return userStorage.getById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        try {
            userStorage.addFriend(userId, friendId);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        try {
            userStorage.removeFriend(userId, friendId);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Set<User> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    public Set<User> getCommonFriends(Long userId, Long otherId) {
        return userStorage.getCommonFriends(userId, otherId);
    }
}