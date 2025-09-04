package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import jakarta.validation.ValidationException;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage storage;

    public Collection<User> getAll() {
        return storage.getAll();
    }

    public User add(User user) {
        validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return storage.add(user);
    }

    public User update(User user) {
        validate(user);
        return storage.update(user);
    }

    public User getById(long id) {
        return storage.getById(id);
    }

    /**
     * Добавление друга:
     * - у отправителя запрос -> UNCONFIRMED
     * - если получатель уже добавил в ответ -> CONFIRMED у обоих
     */
    public void addFriend(long userId, long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);

        // если у друга уже есть этот пользователь
        if (friend.getFriends().containsKey(userId)) {
            user.getFriends().put(friendId, FriendshipStatus.CONFIRMED);
            friend.getFriends().put(userId, FriendshipStatus.CONFIRMED);
        } else {
            user.getFriends().put(friendId, FriendshipStatus.UNCONFIRMED);
        }
    }

    /**
     * Удаление из друзей:
     * - удаляем запись у обоих пользователей
     */
    public void removeFriend(long userId, long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    /**
     * Получить всех друзей пользователя (включая неподтверждённых)
     */
    public Collection<User> getFriends(long userId) {
        User user = getById(userId);
        return user.getFriends().keySet().stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    /**
     * Получить общих друзей (только подтверждённых!)
     */
    public Collection<User> getCommonFriends(long userId, long otherId) {
        User user1 = getById(userId);
        User user2 = getById(otherId);

        return user1.getFriends().entrySet().stream()
                .filter(entry -> entry.getValue() == FriendshipStatus.CONFIRMED) // только подтверждённые
                .map(entry -> entry.getKey())
                .filter(user2.getFriends()::containsKey)
                .map(this::getById)
                .collect(Collectors.toList());
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email должен быть корректным");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(java.time.LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}