package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
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
        if (user.getFriends() == null) {
            user.setFriends(new HashMap<>());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NoSuchElementException("Пользователь не найден: id=" + user.getId());
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashMap<>());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User getById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NoSuchElementException("Пользователь не найден: id=" + id);
        }
        return user;
    }

    public void addFriend(Long userId, Long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.addFriend(friendId, FriendshipStatus.CONFIRMED);
        friend.addFriend(userId, FriendshipStatus.CONFIRMED);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public Set<User> getFriends(Long userId) {
        User user = getById(userId);
        Set<User> friendsSet = new HashSet<>();
        for (Long friendId : user.getFriends().keySet()) {
            friendsSet.add(getById(friendId));
        }
        return friendsSet;
    }

    public Set<User> getCommonFriends(Long userId, Long otherId) {
        Set<User> userFriends = getFriends(userId);
        Set<User> otherFriends = getFriends(otherId);
        userFriends.retainAll(otherFriends);
        return userFriends;
    }

    public void clear() {
        users.clear();
        nextId = 1;
    }
}