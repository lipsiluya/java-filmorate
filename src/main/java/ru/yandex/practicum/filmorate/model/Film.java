package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "films")
@Data
@NoArgsConstructor
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private int duration;

    @ManyToOne
    @JoinColumn(name = "mpa_id")
    private Mpa mpa;

    @ManyToMany
    @JoinTable(
            name = "films_likes",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "films_genres",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    // Конструктор для всех полей
    public Film(Long id, String name, String description, LocalDate releaseDate, int duration,
                Mpa mpa, Set<User> likes, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        if (likes != null) this.likes = likes;
        if (genres != null) this.genres = genres;
    }
}