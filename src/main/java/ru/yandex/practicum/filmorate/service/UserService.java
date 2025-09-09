package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addUser(User user) {
        validateUser(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        return userStorage.updateUser(user);
    }

    public User getUser(Long id) {
        return userStorage.getUser(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    public void addFriend(Long userId, Long friendId) {
        // Проверяем, что пользователи существуют
        getUser(userId);
        getUser(friendId);

        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        // Проверяем, что пользователи существуют
        getUser(userId);
        getUser(friendId);

        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        getUser(userId); // Проверяем существование пользователя
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        getUser(userId);
        getUser(otherId);

        return userStorage.getCommonFriends(userId, otherId);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email должен быть корректным и содержать @");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login не может быть пустым и содержать пробелы");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        // Если имя не указано, используем логин
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}