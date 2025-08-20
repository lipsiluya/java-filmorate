package com.example.service;

import com.example.model.User;
import com.example.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    // --- Работа с друзьями ---
    public void addFriend(Long userId, Long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(Long userId) {
        User user = getById(userId);
        List<User> friends = new ArrayList<>();
        for (Long id : user.getFriends()) {
            friends.add(getById(id));
        }
        return friends;
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = getById(userId);
        User other = getById(otherId);
        Set<Long> common = new HashSet<>(user.getFriends());
        common.retainAll(other.getFriends());

        List<User> result = new ArrayList<>();
        for (Long id : common) {
            result.add(getById(id));
        }
        return result;
    }
}