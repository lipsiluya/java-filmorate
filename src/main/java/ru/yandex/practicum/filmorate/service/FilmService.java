package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    public Film add(Film film) {
        validate(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        validate(film);
        if (filmStorage.getById(film.getId()) == null) {
            throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");
        }
        return filmStorage.update(film);
    }

    public Film getById(long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
        return film;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(long filmId, long userId) {
        Film film = getById(filmId);
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователь " + userId + " не найден");
        }
        film.getLikes().add(userId);
        filmStorage.update(film);
    }

    public void removeLike(long filmId, long userId) {
        Film film = getById(filmId);
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователь " + userId + " не найден");
        }
        film.getLikes().remove(userId);
        filmStorage.update(film);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getAll().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность должна быть положительной");
        }
    }
}