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

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        return filmService.getFilm(id);
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

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getMostPopular(count);
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
        if (film.getMpa() == null || film.getMpa().getId() == null) {
            errors.put("mpaId", "MPA рейтинг обязателен");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}