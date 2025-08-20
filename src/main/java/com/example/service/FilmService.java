package com.example.service;

import com.example.model.Film;
import com.example.storage.FilmStorage;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    public Film add(Film film) {
        validate(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        validate(film);
        return filmStorage.update(film);
    }

    public Film getById(int id) {
        return filmStorage.getById(id);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }
}