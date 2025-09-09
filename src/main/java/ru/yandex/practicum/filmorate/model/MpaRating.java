package ru.yandex.practicum.filmorate.model;

public enum MpaRating {
    G(1),
    PG(2),
    PG_13(3),
    R(4),
    NC_17(5);

    private final int id;

    MpaRating(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MpaRating fromId(long id) {
        for (MpaRating rating : values()) {
            if (rating.getId() == id) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Нет MPA рейтинга с id: " + id);
    }
}