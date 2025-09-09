package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class MpaStorage {
    private final Map<Integer, Mpa> mpaMap = new HashMap<>();

    public MpaStorage() {
        mpaMap.put(1, new Mpa(1, "G"));
        mpaMap.put(2, new Mpa(2, "PG"));
        mpaMap.put(3, new Mpa(3, "PG-13"));
        mpaMap.put(4, new Mpa(4, "R"));
        mpaMap.put(5, new Mpa(5, "NC-17"));
    }

    public Mpa getById(int id) {
        if (!mpaMap.containsKey(id)) throw new NotFoundException("MPA with id=" + id + " not found");
        return mpaMap.get(id);
    }

    public Collection<Mpa> getAll() {
        return mpaMap.values();
    }
}