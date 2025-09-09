package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    // ключ = id друга, значение = статус дружбы
    private Map<Long, FriendshipStatus> friends = new HashMap<>();

    public void addFriend(Long friendId, FriendshipStatus status) {
        friends.put(friendId, status);
    }

    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }
}