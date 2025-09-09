package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "user1@test.com", "user1", "User One", LocalDate.of(1990, 1, 1), null);
        user2 = new User(2L, "user2@test.com", "user2", "User Two", LocalDate.of(1992, 2, 2), null);
    }

    @Test
    void shouldAddUser() throws Exception {
        when(userService.addUser(any(User.class))).thenReturn(user1);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user1.getId()))
                .andExpect(jsonPath("$.login").value(user1.getLogin()));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        user1.setName("Updated Name");
        when(userService.updateUser(any(User.class))).thenReturn(user1);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void shouldHandleFriendshipStatus() throws Exception {
        // Настраиваем сервис, чтобы не выбрасывал исключения
        doNothing().when(userService).addFriend(user1.getId(), user2.getId());

        mockMvc.perform(put("/users/{id}/friends/{friendId}", user1.getId(), user2.getId()))
                .andExpect(status().isOk());

        verify(userService, times(1)).addFriend(user1.getId(), user2.getId());
    }

    @Test
    void shouldRemoveFriend() throws Exception {
        doNothing().when(userService).removeFriend(user1.getId(), user2.getId());

        mockMvc.perform(delete("/users/{id}/friends/{friendId}", user1.getId(), user2.getId()))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).removeFriend(user1.getId(), user2.getId());
    }

    @Test
    void shouldGetUserById() throws Exception {
        when(userService.getUser(user1.getId())).thenReturn(user1);

        mockMvc.perform(get("/users/{id}", user1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1.getId()));
    }
}