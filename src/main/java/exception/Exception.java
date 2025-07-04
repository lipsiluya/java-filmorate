package exception;


/**
 * Пользовательские исключения для приложения Catsgram.
 */
public class Exception {

    /**
     * Исключение, выбрасывается, если не выполнены условия
     * для создания или изменения данных.
     */
    public static class ConditionsNotMetException extends RuntimeException {
        public ConditionsNotMetException(String message) {
            super(message);
        }
    }

    /**
     * Исключение, выбрасывается, если объект не найден.
     */
    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Исключение, выбрасывается при попытке создать или обновить объект с уже существующими уникальными данными.
     */
    public static class DuplicatedDataException extends RuntimeException {
        public DuplicatedDataException(String message) {
            super(message);
        }
    }
}
