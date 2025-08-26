package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    // Добавление нового пользователя
    public User add(User user) {
        validate(user);
        return userStorage.add(user);
    }

    // Обновление пользователя
    public User update(User user) {
        validate(user);
        if (userStorage.getById(user.getId()) == null) {
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден");
        }
        return userStorage.update(user);
    }

    // Получение пользователя по id
    public User getById(long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        return user;
    }

    // Получение всех пользователей
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    // Добавление в друзья
    public void addFriend(long userId, long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.update(user);
        userStorage.update(friend);
    }

    // Удаление из друзей
    public void removeFriend(long userId, long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.update(user);
        userStorage.update(friend);
    }

    // Получение списка друзей
    public List<User> getFriends(long userId) {
        User user = getById(userId);
        return user.getFriends().stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    // Получение общих друзей
    public List<User> getCommonFriends(long userId, long otherId) {
        User user1 = getById(userId);
        User user2 = getById(otherId);
        Set<Long> commonIds = new HashSet<>(user1.getFriends());
        commonIds.retainAll(user2.getFriends());
        return commonIds.stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    // Валидация пользователя
    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email должен быть корректным");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}