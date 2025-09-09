package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmValidator;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.MpaDbStorage;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final JdbcTemplate jdbc;

    public FilmService(FilmRepository filmRepository,
                       UserRepository userRepository,
                       GenreDbStorage genreDbStorage,
                       MpaDbStorage mpaDbStorage,
                       JdbcTemplate jdbc) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.jdbc = jdbc;
    }

    /** Создание фильма */
    public Film addFilm(Film film) {
        FilmValidator.validate(film);
        handleMpaAndGenres(film);
        Film savedFilm = filmRepository.save(film);
        updateFilmGenres(savedFilm);
        return getFilm(savedFilm.getId());
    }

    /** Обновление фильма */
    public Film updateFilm(Film film) {
        FilmValidator.validate(film);
        if (!filmRepository.existsById(film.getId())) {
            throw new NoSuchElementException("Фильм не найден id=" + film.getId());
        }
        handleMpaAndGenres(film);
        Film updatedFilm = filmRepository.save(film);
        updateFilmGenres(updatedFilm);
        return getFilm(updatedFilm.getId());
    }

    /** Получение всех фильмов */
    public List<Film> getAllFilms() {
        List<Film> films = filmRepository.findAll();
        films.forEach(this::loadGenresForFilm);
        return films;
    }

    /** Получение фильма по id с жанрами */
    public Film getFilm(Long id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Фильм не найден id=" + id));
        loadGenresForFilm(film);
        return film;
    }

    /** Добавление лайка */
    public void addLike(Long filmId, Long userId) {
        Film film = getFilm(filmId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден id=" + userId));
        film.getLikes().add(user);
        filmRepository.save(film);
    }

    /** Удаление лайка */
    public void removeLike(Long filmId, Long userId) {
        Film film = getFilm(filmId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден id=" + userId));
        film.getLikes().remove(user);
        filmRepository.save(film);
    }

    /** Получение популярных фильмов по количеству лайков */
    public List<Film> getMostPopular(int count) {
        return filmRepository.findAll().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .peek(this::loadGenresForFilm)
                .toList();
    }

    // --- Вспомогательный метод для обработки MPA и жанров перед сохранением ---
    private void handleMpaAndGenres(Film film) {
        if (film.getMpa() != null && film.getMpa().getId() != null) {
            film.setMpa(mpaDbStorage.getById(film.getMpa().getId()));
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            Set<Genre> genres = film.getGenres().stream()
                    .map(g -> genreDbStorage.getById(g.getId()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            film.setGenres(genres);
        }
    }

    // --- Обновление связей фильм-жанр в базе ---
    private void updateFilmGenres(Film film) {
        jdbc.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbc.update("INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)",
                        film.getId(), genre.getId());
            }
        }
    }

    // --- Подгрузка жанров для фильма ---
    private void loadGenresForFilm(Film film) {
        if (film == null) return;
        Set<Genre> genres = genreDbStorage.getGenresByFilmId(film.getId());
        film.setGenres(genres);
    }
}