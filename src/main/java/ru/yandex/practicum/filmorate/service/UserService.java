package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
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
        return userStorage.update(user);
    }

    public User getById(long id) {
        User user = userStorage.getById(id);
        if (user == null) throw new NotFoundException("Пользователь " + id + " не найден");
        return user;
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
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

    public Collection<User> getFriends(long userId) {
        User user = getById(userId);
        Set<Long> friendIds = user.getFriends();
        return friendIds.stream()
                .map(this::getById)
                .collect(Collectors.toSet());
    }

    public Collection<User> getCommonFriends(long userId, long otherId) {
        User user1 = getById(userId);
        User user2 = getById(otherId);

        Set<Long> commonIds = new HashSet<>(user1.getFriends());
        commonIds.retainAll(user2.getFriends());

        return commonIds.stream()
                .map(this::getById)
                .collect(Collectors.toSet());
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email должен быть корректным и не пустым");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}