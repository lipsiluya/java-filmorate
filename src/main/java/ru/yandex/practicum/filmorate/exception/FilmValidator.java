package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FilmValidator {

    private static final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);

    public static void validate(Film film) {
        Map<String, String> errors = new HashMap<>();

        if (film.getName() == null || film.getName().isBlank()) {
            errors.put("name", "Название фильма не может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            errors.put("description", "Описание не должно превышать 200 символов");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            errors.put("releaseDate", "Дата релиза не может быть раньше 28.12.1895");
        }

        if (film.getDuration() <= 0) {
            errors.put("duration", "Продолжительность должна быть положительной");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}