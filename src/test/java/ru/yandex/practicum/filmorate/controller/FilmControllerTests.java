package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureMockMvc
class FilmControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FilmStorage filmStorage;

    private Film validFilm;

    @BeforeEach
    void setUp() {
        // Очищаем базу перед каждым тестом
        filmStorage.getAll().forEach(film -> filmStorage.delete(film.getId()));

        validFilm = new Film(
                null,
                "Inception",
                "A film by Christopher Nolan",
                LocalDate.of(2010, 7, 16),
                148,
                new Mpa((long) MpaRating.PG_13.getId(), null)
        );
    }

    @Test
    void shouldReturnEmptyFilmListInitially() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldCreateValidFilm() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Inception"))
                .andExpect(jsonPath("$.mpaId").value(MpaRating.PG_13.getId()));
    }

    @Test
    void shouldReturnValidationErrorForEmptyName() throws Exception {
        Film invalidFilm = new Film(
                null,
                "",
                validFilm.getDescription(),
                validFilm.getReleaseDate(),
                validFilm.getDuration(),
                validFilm.getMpa()
        );

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFilm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Название фильма не может быть пустым"));
    }

    @Test
    void shouldUpdateExistingFilm() throws Exception {
        String response = mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Film createdFilm = objectMapper.readValue(response, Film.class);

        Film updatedFilm = new Film(
                createdFilm.getId(),
                "Updated Title",
                createdFilm.getDescription(),
                createdFilm.getReleaseDate(),
                createdFilm.getDuration(),
                createdFilm.getMpa()
        );

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFilm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Title"))
                .andExpect(jsonPath("$.mpaId").value(MpaRating.PG_13.getId()));
    }
}