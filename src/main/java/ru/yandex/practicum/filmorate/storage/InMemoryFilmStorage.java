package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    public Collection<Film> getAll() {
        return films.values();
    }

    public Film add(Film film) {
        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NoSuchElementException("Фильм не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film getById(long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NoSuchElementException("Фильм не найден");
        }
        return film;
    }
}