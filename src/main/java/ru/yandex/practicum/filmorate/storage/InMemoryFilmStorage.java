package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class InMemoryFilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    // Получить все фильмы
    public Collection<Film> getAll() {
        return films.values();
    }

    // Добавить новый фильм
    public Film add(Film film) {
        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    // Обновить существующий фильм
    public Film update(Film film) {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            throw new NoSuchElementException("Фильм не найден: id=" + film.getId());
        }
        films.put(film.getId(), film);
        return film;
    }

    // Получить фильм по id
    public Film getById(Long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NoSuchElementException("Фильм не найден: id=" + id);
        }
        return film;
    }

    // Удалить фильм
    public void delete(Long id) {
        films.remove(id);
    }

    // Метод для тестов: полностью очищает хранилище и сбрасывает счетчик id
    public void clear() {
        films.clear();
        nextId = 1;
    }
}