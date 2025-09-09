package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage {

    private final JdbcTemplate jdbc;

    public List<Genre> getAll() {
        return jdbc.query("SELECT * FROM genres", (rs, rowNum) ->
                new Genre(rs.getLong("id"), rs.getString("name"))
        );
    }

    public Genre getById(Long id) {
        return jdbc.query("SELECT * FROM genres WHERE id = ?", (rs, rowNum) ->
                        new Genre(rs.getLong("id"), rs.getString("name")), id)
                .stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Жанр не найден"));
    }
}