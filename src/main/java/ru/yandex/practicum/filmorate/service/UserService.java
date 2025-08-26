package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User add(@Valid User user) {
        return userStorage.add(user);
    }

    public User update(@Valid User user) {
        User existing = userStorage.getById(user.getId());
        if (existing == null) throw new NotFoundException("Пользователь " + user.getId() + " не найден");
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

    public Collection<User> getFriends(long userId) {
        User user = getById(userId);
        Set<Long> friendIds = user.getFriends();
        return friendIds.stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(long userId, long otherUserId) {
        User user1 = getById(userId);
        User user2 = getById(otherUserId);

        Set<Long> commonIds = user1.getFriends();
        commonIds.retainAll(user2.getFriends());

        return commonIds.stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    public void addFriend(long userId, long friendId) {
        User user = getById(userId);
        getById(friendId); // проверка существования
        user.getFriends().add(friendId);
        userStorage.update(user);
    }

    public void removeFriend(long userId, long friendId) {
        User user = getById(userId);
        getById(friendId); // проверка существования
        user.getFriends().remove(friendId);
        userStorage.update(user);
    }
}