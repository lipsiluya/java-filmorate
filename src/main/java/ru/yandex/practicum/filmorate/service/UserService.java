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

    public User addUser(User user) {
        validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.add(user);
    }

    public User update(User user) {
        validate(user);
        // проверим, что пользователь существует
        getById(requireId(user));
        return userStorage.update(user);
    }

    public User getById(long id) {
        User u = userStorage.getById(id);
        if (u == null) throw new NotFoundException("Пользователь " + id + " не найден");
        return u;
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    // ------- FRIENDS -------
    public void addFriend(long id, long friendId) {
        User u1 = getById(id);
        User u2 = getById(friendId);
        u1.getFriends().add(u2.getId());
        u2.getFriends().add(u1.getId());
        userStorage.update(u1);
        userStorage.update(u2);
    }

    public void removeFriend(long id, long friendId) {
        User u1 = getById(id);
        User u2 = getById(friendId);
        u1.getFriends().remove(u2.getId());
        u2.getFriends().remove(u1.getId());
        userStorage.update(u1);
        userStorage.update(u2);
    }

    public List<User> getFriends(long id) {
        User u = getById(id);
        return u.getFriends().stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {
        Set<Long> f1 = new HashSet<>(getById(id).getFriends());
        Set<Long> f2 = new HashSet<>(getById(otherId).getFriends());
        f1.retainAll(f2);
        return f1.stream().map(this::getById).collect(Collectors.toList());
    }

    // ------- helpers -------
    private void validate(User user) {
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private long requireId(User u) {
        if (u.getId() == null) throw new ValidationException("id обязателен для обновления пользователя");
        return u.getId();
    }
}