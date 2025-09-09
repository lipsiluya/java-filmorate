package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    private Set<Long> likes = new HashSet<>();

    // Конструктор без id (для создания нового фильма)
    public Film(String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    // Конструктор с id (для обновления или получения из базы)
    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    // Новый конструктор с likes (для тестов и FilmRowMapper)
    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa, Set<Long> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.likes = likes != null ? likes : new HashSet<>();
    }

    // Для удобства JSON сериализации/десериализации
    public Long getMpaId() {
        return mpa != null ? mpa.getId() : null;
    }

    public void setMpaId(Long mpaId) {
        if (this.mpa == null) this.mpa = new Mpa();
        this.mpa.setId(mpaId);
    }
}