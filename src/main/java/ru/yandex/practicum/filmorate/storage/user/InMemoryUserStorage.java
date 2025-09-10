/*package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("m")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final UserValidator validator = new UserValidator();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {

        // формируем дополнительные данные
        user.setId(getNextId());
        log.info("пользователю присвоен id");

        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        log.info("пользователь добавлен в базу");
        return user;
    }


    public Optional<User> update(User newUser) {
        if (!users.containsKey(newUser.getId())) {
            return Optional.empty();
        }

        // проверяем нового пользователя целиком
        validator.validate(newUser);

        User oldUser = users.get(newUser.getId());

        oldUser.setName(newUser.getName());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setBirthday(newUser.getBirthday());

        if (newUser.getFriends() != null) {
            oldUser.setFriends(newUser.getFriends());
            log.info("обновлены друзья пользователя");
        }

        if (newUser.getLikes() != null) {
            oldUser.setLikes(newUser.getLikes());
            log.info("обновлены лайки пользователя");
        }

        log.info("пользователь обновлён: {}", oldUser.getId());
        return Optional.of(oldUser);
    }

    @Override
    public void addFriendship(Integer userId, Integer friendId) {
        users.get(userId).getFriends().add(friendId);
    }

    @Override
    public List<User> findFriendsByUser(Integer id) {
        User user = users.get(id);
        return user.getFriends().stream()
                .map(users::get)
                .collect(Collectors.toList());

    }

    @Override
    public List<User> showCommonFriends(Integer userId, Integer friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        return user.getFriends().stream()
                .filter(id -> friend.getFriends().contains(id))
                .map(users::get)
                .collect(Collectors.toList());
    }

    public void addLike(Integer filmId, Integer userId) {
        users.get(userId).getLikes().add(filmId);
    }


    // вспомогательный метод для генерации идентификатора нового пользователя
    private Integer getNextId() {
        log.info("создан id");
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }


    public Optional<User> findUserById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void removeFriendship(Integer userId, Integer friendId) {
        users.get(userId).getFriends().remove(friendId);
    }

    public boolean isUserExist(Integer id) {
        return users.containsKey(id);
    }

    public boolean isMailExist(String mail) {
        return users.values()
                .stream()
                .map(User::getEmail)
                .anyMatch(m -> m.equals(mail));
    }
}
*/