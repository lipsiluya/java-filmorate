package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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
        user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@test.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));

        user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@test.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void addUser_WithValidData_ShouldReturnCreated() throws Exception {
        when(userService.addUser(any(User.class))).thenReturn(user1);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user1@test.com"))
                .andExpect(jsonPath("$.login").value("user1"));
    }

    @Test
    void updateUser_WithValidData_ShouldReturnOk() throws Exception {
        user1.setName("Updated Name");
        when(userService.updateUser(any(User.class))).thenReturn(user1);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void getUser_WithValidId_ShouldReturnUser() throws Exception {
        when(userService.getUser(1L)).thenReturn(user1);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("user1"));
    }

    @Test
    void addFriend_WithValidIds_ShouldReturnOk() throws Exception {
        doNothing().when(userService).addFriend(1L, 2L);

        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk());

        verify(userService, times(1)).addFriend(1L, 2L);
    }

    @Test
    void removeFriend_WithValidIds_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).removeFriend(1L, 2L);

        mockMvc.perform(delete("/users/1/friends/2"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).removeFriend(1L, 2L);
    }

    @Test
    void getFriends_WithValidId_ShouldReturnFriendsList() throws Exception {
        when(userService.getFriends(1L)).thenReturn(List.of(user2));

        mockMvc.perform(get("/users/1/friends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void getCommonFriends_WithValidIds_ShouldReturnCommonFriends() throws Exception {
        when(userService.getCommonFriends(1L, 3L)).thenReturn(List.of(user2));

        mockMvc.perform(get("/users/1/friends/common/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2));
    }
}