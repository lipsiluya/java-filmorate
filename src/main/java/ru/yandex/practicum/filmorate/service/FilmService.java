package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FilmService {

    private final InMemoryFilmStorage filmStorage;

    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            throw new NoSuchElementException("Фильм не найден id=null");
        }
        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return List.copyOf(filmStorage.getAll());
    }

    public Film getFilm(Long id) {
        return filmStorage.getById(id);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = getFilm(filmId);
        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = getFilm(filmId);
        film.getLikes().remove(userId);
    }

    public List<Film> getMostPopular(int count) {
        return filmStorage.getAll().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .toList();
    }
}