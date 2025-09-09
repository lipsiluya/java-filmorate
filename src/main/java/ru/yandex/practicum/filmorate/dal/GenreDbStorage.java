package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage {

    private final JdbcTemplate jdbc;

    /** Получение всех жанров */
    public List<Genre> getAll() {
        return jdbc.query("SELECT * FROM genres ORDER BY id", (rs, rowNum) ->
                new Genre(rs.getLong("id"), rs.getString("name"))
        );
    }

    /** Получение жанра по id */
    public Genre getById(Long id) {
        return jdbc.query("SELECT * FROM genres WHERE id = ?", (rs, rowNum) ->
                        new Genre(rs.getLong("id"), rs.getString("name")), id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Жанр с id=" + id + " не найден"));
    }

    /** Получение всех жанров для конкретного фильма */
    public Set<Genre> getGenresByFilmId(Long filmId) {
        List<Genre> genres = jdbc.query(
                "SELECT g.id, g.name " +
                        "FROM genres g " +
                        "JOIN film_genres fg ON g.id = fg.genre_id " +
                        "WHERE fg.film_id = ? " +
                        "ORDER BY g.id",
                (rs, rowNum) -> new Genre(rs.getLong("id"), rs.getString("name")),
                filmId
        );
        return new LinkedHashSet<>(genres); // сохраняем порядок
    }
}