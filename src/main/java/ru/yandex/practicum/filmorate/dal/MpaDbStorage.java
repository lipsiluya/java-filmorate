package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage {

    private final JdbcTemplate jdbc;

    public List<Mpa> getAll() {
        return jdbc.query("SELECT * FROM mpa ORDER BY id", (rs, rowNum) ->
                new Mpa(rs.getLong("id"), rs.getString("name"))
        );
    }

    public Mpa getById(Long id) {
        return jdbc.query("SELECT * FROM mpa WHERE id = ?", (rs, rowNum) ->
                        new Mpa(rs.getLong("id"), rs.getString("name")), id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Рейтинг не найден id=" + id));
    }
}