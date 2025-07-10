package com.example.exception;

/**
 * Класс, содержащий пользовательские исключения для приложения.
 * Переименован, чтобы не конфликтовать с java.lang.Exception
 */
public class CustomExceptions {

    public static class ConditionsNotMetException extends RuntimeException {
        public ConditionsNotMetException(String message) {
            super(message);
        }
    }

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public static class DuplicatedDataException extends RuntimeException {
        public DuplicatedDataException(String message) {
            super(message);
        }
    }
}