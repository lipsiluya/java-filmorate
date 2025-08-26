package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public User add(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId()))
            return null; // сервис обработает
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(Long id) {
        return users.get(id); // возвращаем null
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }
}