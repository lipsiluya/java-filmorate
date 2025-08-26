package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    public Film add(Film film) {
        validateFilm(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        if (film.getId() == null || filmStorage.getById(film.getId()) == null) {
            throw new NotFoundException("Фильм " + film.getId() + " не найден");
        }
        validateFilm(film);
        return filmStorage.update(film);
    }

    public Film getById(long id) {
        Film film = filmStorage.getById(id);
        if (film == null) throw new NotFoundException("Фильм " + id + " не найден");
        return film;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(long filmId, long userId) {
        Film film = getById(filmId);
        if (userId <= 0) {
            throw new NotFoundException("Пользователь " + userId + " не найден");
        }
        film.getLikes().add(userId);
        filmStorage.update(film);
    }

    public void removeLike(long filmId, long userId) {
        Film film = getById(filmId);
        if (userId <= 0) {
            throw new NotFoundException("Пользователь " + userId + " не найден");
        }
        film.getLikes().remove(userId);
        filmStorage.update(film);
    }

    public Collection<Film> getPopular(int count) {
        return filmStorage.getAll().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .toList();
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>()); // защита от NPE
        }
    }
}