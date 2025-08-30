package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import jakarta.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage storage;
    private static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    public Collection<Film> getAll() {
        return storage.getAll();
    }

    public Film add(Film film) {
        validate(film);
        return storage.add(film);
    }

    public Film update(Film film) {
        validate(film);
        return storage.update(film);
    }

    public Film getById(long id) {
        return storage.getById(id);
    }

    public void addLike(long filmId, long userId) {
        Film film = getById(filmId);
        film.getLikes().add(userId);
    }

    public void removeLike(long filmId, long userId) {
        Film film = getById(filmId);
        film.getLikes().remove(userId);
    }

    public Collection<Film> getPopular(int count) {
        return storage.getAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .toList();
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate() == null) {
            throw new ValidationException("Дата релиза не может быть пустой");
        }
        if (film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
        if (film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата релиза не может быть в будущем");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность должна быть положительной");
        }
    }
}