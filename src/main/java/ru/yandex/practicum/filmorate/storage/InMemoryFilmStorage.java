package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    @Override
    public Film add(Film film) {
        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            return null; // возвращаем null, чтобы сервис обработал
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(Long id) {
        return films.get(id); // возвращаем null, чтобы сервис обработал
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }
}