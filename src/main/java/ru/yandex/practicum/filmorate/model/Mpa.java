package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mpa {

    private Long id;
    private String name;

    public Mpa(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}