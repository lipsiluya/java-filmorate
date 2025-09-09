package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mpa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}