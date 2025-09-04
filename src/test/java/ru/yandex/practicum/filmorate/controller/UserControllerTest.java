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
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InMemoryUserStorage userStorage;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userStorage.getAll().clear();

        user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));

        user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
    }

    @Test
    void addUser_EmptyEmail_BadRequest() throws Exception {
        user1.setEmail("");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email не может быть пустым"));
    }

    @Test
    void addUser_BirthdayInFuture_BadRequest() throws Exception {
        user1.setBirthday(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.birthday").value("Дата рождения не может быть в будущем"));
    }

    @Test
    void shouldHandleFriendshipStatus() throws Exception {
        // Создаем пользователей
        String response1 = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String response2 = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user2)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        User createdUser1 = mapper.readValue(response1, User.class);
        User createdUser2 = mapper.readValue(response2, User.class);

        // user1 отправляет запрос дружбы user2
        mockMvc.perform(put("/users/{id}/friends/{friendId}", createdUser1.getId(), createdUser2.getId()))
                .andExpect(status().isOk());

        // Проверяем, что у user1 статус UNCONFIRMED
        mockMvc.perform(get("/users/{id}", createdUser1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friends['" + createdUser2.getId() + "']").value(FriendshipStatus.UNCONFIRMED.toString()));

        // user2 подтверждает дружбу
        mockMvc.perform(put("/users/{id}/friends/{friendId}", createdUser2.getId(), createdUser1.getId()))
                .andExpect(status().isOk());// Проверяем, что дружба стала CONFIRMED у обоих
        mockMvc.perform(get("/users/{id}", createdUser1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friends['" + createdUser2.getId() + "']").value(FriendshipStatus.CONFIRMED.toString()));

        mockMvc.perform(get("/users/{id}", createdUser2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friends['" + createdUser1.getId() + "']").value(FriendshipStatus.CONFIRMED.toString()));
    }
}