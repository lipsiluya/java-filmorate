package ru.yandex.practicum.filmorate.exception;

import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, String> errors;

    // Конструктор для одиночной ошибки
    public ValidationException(String message) {
        super(message);
        this.errors = Map.of("error", message);
    }

    // Конструктор для нескольких ошибок
    public ValidationException(Map<String, String> errors) {
        super("Ошибка валидации");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}