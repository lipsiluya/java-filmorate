package ru.yandex.practicum.filmorate.exception;

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