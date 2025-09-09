package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String login;

    private String name;

    private LocalDate birthday;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Friendship> friendships = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void preSave() {
        if (name == null || name.isBlank()) {
            name = login;
        }
        if (birthday != null && birthday.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата рождения не может быть в будущем");
        }
    }
}