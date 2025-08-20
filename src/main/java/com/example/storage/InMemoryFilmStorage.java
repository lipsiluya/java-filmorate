package com.example.storage;

import com.example.model.Film;
import com.example.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @Override
    public Film add(Film film) {
        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(int id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }
}