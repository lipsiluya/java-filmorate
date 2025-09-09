package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UserValidator {

    public static void validate(User user) {
        Map<String, String> errors = new HashMap<>();

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            errors.put("login", "Логин не может быть пустым или содержать пробелы");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            errors.put("birthday", "Дата рождения не может быть в будущем");
        }

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            errors.put("email", "Email должен быть валидным");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}