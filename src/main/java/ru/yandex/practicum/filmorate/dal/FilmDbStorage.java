package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage {

    private final JdbcTemplate jdbc;
    private final FilmRowMapper mapper;

    private static final String INSERT_SQL =
            "INSERT INTO films(name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL =
            "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM films";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM films WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM films WHERE id = ?";

    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setObject(5, film.getMpaId());
            return ps;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return film;
    }

    public Film update(Film film) {
        int updated = jdbc.update(UPDATE_SQL, film.getName(), film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()), film.getDuration(),
                film.getMpaId(), film.getId());
        if (updated == 0) {
            throw new NoSuchElementException("Фильм не найден id=" + film.getId());
        }
        return film;
    }

    public Film getById(Long id) {
        return jdbc.query(SELECT_BY_ID_SQL, mapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Фильм не найден id=" + id));
    }

    public List<Film> getAll() {
        return jdbc.query(SELECT_ALL_SQL, mapper);
    }

    public void delete(Long id) {
        jdbc.update(DELETE_SQL, id);
    }
}