package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        validateUser(user); // Добавьте эту проверку!
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validateUser(user); // И здесь!
        return userService.updateUser(user);
    }

    private void validateUser(User user) {
        Map<String, String> errors = new HashMap<>();

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            errors.put("email", "Email должен быть корректным и содержать @");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            errors.put("login", "Login не может быть пустым и содержать пробелы");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            errors.put("birthday", "Дата рождения не может быть в будущем");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // Если имя не указано, используем логин
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    // === Эндпоинты для работы с друзьями ===

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    // === Обработчики ошибок ===
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(ValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(NoSuchElementException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgumentException(IllegalArgumentException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleOtherExceptions(Exception e) {
        return Map.of("error", "Внутренняя ошибка сервера: " + e.getMessage());
    }
}