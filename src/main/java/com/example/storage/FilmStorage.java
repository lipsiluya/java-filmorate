package com.example.storage;

import com.example.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film add(Film film);

    Film update(Film film);

    Film getById(int id);

    Collection<Film> getAll();
}