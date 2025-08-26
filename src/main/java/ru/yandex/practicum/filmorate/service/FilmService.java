package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    public Film add(Film film) {
        validateFilm(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        validateFilm(film);
        Film existing = filmStorage.getById(film.getId());
        if (existing == null) {
            throw new NotFoundException("Фильм " + film.getId() + " не найден");
        }
        return filmStorage.update(film);
    }

    public Film getById(long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new NotFoundException("Фильм " + id + " не найден");
        }
        return film;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(long filmId, long userId) {
        Film film = getById(filmId);
        User user = userStorage.getById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь " + userId + " не найден");
        }
        film.getLikes().add(userId);
        filmStorage.update(film);
    }

    public void removeLike(long filmId, long userId) {
        Film film = getById(filmId);
        User user = userStorage.getById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь " + userId + " не найден");
        }
        film.getLikes().remove(userId);
        filmStorage.update(film);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }
}