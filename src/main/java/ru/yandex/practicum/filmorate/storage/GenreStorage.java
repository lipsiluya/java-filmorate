package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class GenreStorage {
    private final Map<Integer, Genre> genreMap = new HashMap<>();

    public GenreStorage() {
        genreMap.put(1, new Genre(1, "Комедия"));
        genreMap.put(2, new Genre(2, "Драма"));
        genreMap.put(3, new Genre(3, "Мультфильм"));
        genreMap.put(4, new Genre(4, "Триллер"));
        genreMap.put(5, new Genre(5, "Документальный"));
    }

    public Genre getById(int id) {
        if (!genreMap.containsKey(id)) throw new NotFoundException("Genre with id=" + id + " not found");
        return genreMap.get(id);
    }

    public Collection<Genre> getAll() {
        return genreMap.values();
    }
}