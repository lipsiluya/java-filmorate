package com.example.storage;

import com.example.model.User;

import java.util.Collection;

public interface UserStorage {
    User add(User user);

    User update(User user);

    User getById(Long id);

    Collection<User> getAll();
}