package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

@Service
public class UserService {

    private final UserStorage userStorage;

    // Внедрение через конструктор
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.add(user); // <-- метод из интерфейса
    }

    // Можно добавить другие методы: update, getById, getAll
}