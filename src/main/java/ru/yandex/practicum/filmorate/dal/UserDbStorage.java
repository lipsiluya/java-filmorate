package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;

    @Override
    public User add(User user) {
        String sql = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
        var keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, user.getBirthday() != null ? java.sql.Date.valueOf(user.getBirthday()) : null);
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        int updated = jdbc.update(sql, user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday() != null ? java.sql.Date.valueOf(user.getBirthday()) : null,
                user.getId());
        if (updated == 0) throw new NoSuchElementException("Пользователь не найден id=" + user.getId());
        return user;
    }

    @Override
    public User getById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = jdbc.query(sql, mapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден id=" + id));
        loadFriends(user);
        return user;
    }

    @Override
    public Collection<User> getAll() {
        List<User> users = jdbc.query("SELECT * FROM users", mapper);
        users.forEach(this::loadFriends);
        return users;
    }

    @Override
    public void addFriend(Long userId, Long friendId, FriendshipStatus status) {
        if (status == null || userId.equals(friendId)) return;
        if (!exists(userId) || !exists(friendId)) {
            throw new NoSuchElementException("Пользователь не найден");
        }

        String deleteSql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbc.update(deleteSql, userId, friendId);

        String insertSql = "INSERT INTO friends(user_id, friend_id, status) VALUES (?, ?, ?)";
        jdbc.update(insertSql, userId, friendId, status.name());

        if (status == FriendshipStatus.CONFIRMED) {
            jdbc.update(deleteSql, friendId, userId);
            jdbc.update(insertSql, friendId, userId, status.name());
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        jdbc.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?", userId, friendId);
    }

    // === Новые методы ===

    public Set<User> getFriends(Long userId) {
        if (!exists(userId)) {
            throw new NoSuchElementException("Пользователь не найден id=" + userId);
        }

        String sql = "SELECT u.* FROM users u " +
                "JOIN friends f ON u.id = f.friend_id " +
                "WHERE f.user_id = ? AND f.status = ?";
        List<User> friends = jdbc.query(sql, mapper, userId, FriendshipStatus.CONFIRMED.name());
        return new HashSet<>(friends);
    }

    public Set<User> getCommonFriends(Long userId, Long otherId) {
        if (!exists(userId) || !exists(otherId)) {
            throw new NoSuchElementException("Пользователь не найден");
        }

        String sql = "SELECT u.* FROM users u " +
                "JOIN friends f1 ON u.id = f1.friend_id " +
                "JOIN friends f2 ON u.id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ? " +
                "AND f1.status = ? AND f2.status = ?";
        List<User> commonFriends = jdbc.query(sql, mapper, userId, otherId,
                FriendshipStatus.CONFIRMED.name(), FriendshipStatus.CONFIRMED.name());
        return new HashSet<>(commonFriends);
    }

    // === Вспомогательные методы ===

    private void loadFriends(User user) {
        String sql = "SELECT friend_id, status FROM friends WHERE user_id = ?";
        List<FriendRecord> friends = jdbc.query(sql, (rs, rowNum) ->
                        new FriendRecord(rs.getLong("friend_id"), FriendshipStatus.valueOf(rs.getString("status"))),
                user.getId());
        user.getFriends().clear();
        for (FriendRecord f : friends) {
            user.getFriends().put(f.friendId, f.status);
        }
    }

    private boolean exists(Long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    private static class FriendRecord {
        long friendId;
        FriendshipStatus status;

        FriendRecord(long friendId, FriendshipStatus status) {
            this.friendId = friendId;
            this.status = status;
        }
    }
}