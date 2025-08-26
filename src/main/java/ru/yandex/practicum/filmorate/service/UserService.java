package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User add(@Valid User user) {
        if (user.getFriends() == null) {
            user.setFriends(Set.of());
        }
        return userStorage.add(user);
    }

    public User update(@Valid User user) {
        return userStorage.update(user);
    }

    public User getById(long id) {
        try {
            return userStorage.getById(id);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Пользователь " + id + " не найден");
        }
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    // Получение друзей
    public Collection<User> getFriends(long userId) {
        User user = getById(userId);
        return user.getFriends().stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    // Общие друзья
    public Collection<User> getCommonFriends(long userId, long otherUserId) {
        User user1 = getById(userId);
        User user2 = getById(otherUserId);

        Set<Long> commonIds = user1.getFriends();
        commonIds.retainAll(user2.getFriends());

        return commonIds.stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    // Добавление дружбы (двусторонняя)
    public void addFriend(long userId, long friendId) {
        User user1 = getById(userId);
        User user2 = getById(friendId);

        user1.getFriends().add(friendId);
        user2.getFriends().add(userId);

        userStorage.update(user1);
        userStorage.update(user2);
    }

    // Удаление дружбы (двусторонняя)
    public void removeFriend(long userId, long friendId) {
        User user1 = getById(userId);
        User user2 = getById(friendId);

        user1.getFriends().remove(friendId);
        user2.getFriends().remove(userId);

        userStorage.update(user1);
        userStorage.update(user2);
    }
}