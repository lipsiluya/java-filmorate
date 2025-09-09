package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User add(User user);

    User update(User user);

    User getById(Long id);

    Collection<User> getAll();

    void addFriend(Long userId, Long friendId, FriendshipStatus status);

    void removeFriend(Long userId, Long friendId);
}