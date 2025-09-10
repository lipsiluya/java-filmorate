/*package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final FilmValidator validator = new FilmValidator();

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film create(Film film) {

        // формируем дополнительные данные
        film.setId(getNextId());
        log.info("фильму присвоен id");
        // сохраняем новый фильм в памяти приложения
        films.put(film.getId(), film);
        log.info("фильм добавлен в базу");

        return film;
    }

    public Optional<Film> update(Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            return Optional.empty();
        }

        // проверяем весь фильм целиком
        validator.validate(newFilm);

        Film oldFilm = films.get(newFilm.getId());

        oldFilm.setName(newFilm.getName());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());

        log.info("фильм обновлён: {}", oldFilm.getId());

        return Optional.of(oldFilm);
    }


    public Optional<Film> findFilmById(Integer id) {
        return Optional.of(films.get(id));
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        films.get(filmId).getLikes().add(userId);
    }

    public boolean isFilmExist(Integer id) {
        return films.containsKey(id);
    }


    public boolean isFilmExist(String name) {
        return films.values()
                .stream()
                .map(Film::getName)
                .anyMatch(f -> f.equals(name));
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        films.get(filmId).getLikes().remove(userId);
    }

    public List<Film> getTopFilm(long count) {

        return findAll().stream()
                .sorted(Comparator.comparingLong((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }


    // вспомогательный метод для генерации идентификатора нового пользователя
    private Integer getNextId() {
        log.info("вызван метод создания id");
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
*/