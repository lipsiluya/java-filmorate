package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User add(User user) {
        validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.add(user);
    }

    public User update(User user) {
        validate(user);
        if (!userExists(user.getId())) {
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден");
        }
        return userStorage.update(user);
    }

    public User getById(long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        return user;
    }

    public List<User> getAll() {
        return userStorage.getAll().stream().toList();
    }

    public void addFriend(long userId, long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.update(user);
        userStorage.update(friend);
    }

    public void removeFriend(long userId, long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.update(user);
        userStorage.update(friend);
    }

    public Set<User> getFriends(long userId) {
        User user = getById(userId);
        return user.getFriends().stream()
                .map(this::getById)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(long userId, long otherId) {
        Set<Long> friends1 = getById(userId).getFriends();
        Set<Long> friends2 = getById(otherId).getFriends();
        return friends1.stream()
                .filter(friends2::contains)
                .map(this::getById)
                .collect(Collectors.toSet());
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email должен быть корректным");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new IllegalArgumentException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата рождения не может быть в будущем");
        }
    }

    private boolean userExists(Long id) {
        try {
            return userStorage.getById(id) != null;
        } catch (NotFoundException e) {
            return false;
        }
    }
}