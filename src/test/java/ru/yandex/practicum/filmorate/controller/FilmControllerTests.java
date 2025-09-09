package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
class FilmControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;

    private Film validFilm;

    @BeforeEach
    void setUp() {
        Mpa mpa = new Mpa();
        mpa.setId((long) MpaRating.PG_13.getId());
        mpa.setName("PG-13");

        validFilm = new Film();
        validFilm.setId(1L);
        validFilm.setName("Inception");
        validFilm.setDescription("A film by Christopher Nolan");
        validFilm.setReleaseDate(LocalDate.of(2010, 7, 16));
        validFilm.setDuration(148);
        validFilm.setMpa(mpa);
    }

    @Test
    void getAllFilms_ShouldReturnListOfFilms() throws Exception {
        when(filmService.getAllFilms()).thenReturn(List.of(validFilm));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Inception"));
    }

    @Test
    void addFilm_WithValidData_ShouldReturnCreated() throws Exception {
        when(filmService.addFilm(any(Film.class))).thenReturn(validFilm);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Inception"))
                .andExpect(jsonPath("$.mpa.id").value(MpaRating.PG_13.getId()));
    }

    @Test
    void updateFilm_WithValidData_ShouldReturnOk() throws Exception {
        validFilm.setName("Updated Inception");
        when(filmService.updateFilm(any(Film.class))).thenReturn(validFilm);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Inception"));
    }

    @Test
    void getFilm_WithValidId_ShouldReturnFilm() throws Exception {
        when(filmService.getFilm(1L)).thenReturn(validFilm);

        mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Inception"));
    }

    @Test
    void addLike_WithValidIds_ShouldReturnOk() throws Exception {
        doNothing().when(filmService).addLike(1L, 1L);

        mockMvc.perform(put("/films/1/like/1"))
                .andExpect(status().isOk());

        verify(filmService, times(1)).addLike(1L, 1L);
    }

    @Test
    void removeLike_WithValidIds_ShouldReturnOk() throws Exception {
        doNothing().when(filmService).removeLike(1L, 1L);

        mockMvc.perform(delete("/films/1/like/1"))
                .andExpect(status().isOk());

        verify(filmService, times(1)).removeLike(1L, 1L);
    }
//f
    @Test
    void getMostPopular_ShouldReturnPopularFilms() throws Exception {
        when(filmService.getMostPopular(10)).thenReturn(List.of(validFilm));

        mockMvc.perform(get("/films/popular")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }
}