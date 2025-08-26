package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    public Collection<User> getAll() {
        return users.values();
    }

    public User add(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        users.put(user.getId(), user);
        return user;
    }

    public User getById(long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        return user;
    }
}