package ru.yandex.practicum.filmorate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    // Здесь пока не нужны дополнительные методы, стандартного JpaRepository хватает
}