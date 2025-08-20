package com.example.storage;

import com.example.exception.NotFoundException;
import com.example.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public User add(User user) {
        user.setId(nextId++);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return users.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }
}