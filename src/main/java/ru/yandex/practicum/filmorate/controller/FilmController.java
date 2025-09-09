package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@RequestBody Film film) {
        validateFilm(film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateFilm(film);
        return filmService.updateFilm(film);
    }

    private void validateFilm(Film film) {
        Map<String, String> errors = new HashMap<>();
        if (film.getName() == null || film.getName().isBlank()) {
            errors.put("name", "Название фильма не может быть пустым");
        }
        if (film.getDuration() <= 0) {
            errors.put("duration", "Продолжительность фильма должна быть положительной");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            errors.put("releaseDate", "Неверная дата релиза фильма");
        }
        if (film.getMpaId() == null) {
            errors.put("mpaId", "MPA рейтинг обязателен");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}