package tests;

import controller.UserController;
import exception.GlobalExceptionHandler;
import exception.ValidationException;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {
    private MockMvc mockMvc;
    private UserController userController;

    @BeforeEach
    public void setup() {
        userController = new UserController();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void addUser_ValidUser_Success() {
        String json = """
                {
                    "email": "test@example.com",
                    "login": "testuser",
                    "name": "Test User",
                    "birthday": "1990-01-01"
                }
                """;

        try {
            mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.name").value("Test User"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addUser_EmptyEmail_BadRequest() {
        String json = """
                {
                    "email": "",
                    "login": "testuser",
                    "birthday": "1990-01-01"
                }
                """;

        try {
            mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest()).andExpect(content().string(containsString("Email не может быть пустым")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addUser_InvalidLogin_BadRequest() {
        String json = """
                {
                    "email": "test@example.com",
                    "login": "invalid login",
                    "birthday": "1990-01-01"
                }
                """;

        try {
            mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest()).andExpect(content().string(containsString("Логин не может быть пустым")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addUser_BirthdayInFuture_BadRequest() {
        String json = String.format("""
                {
                    "email": "test@example.com",
                    "login": "testuser",
                    "birthday": "%s"
                }
                """, LocalDate.now().plusDays(1));

        try {
            mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest()).andExpect(content().string(containsString("Дата рождения не может быть в будущем")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void validateUserMethod_ShouldThrowValidationException() {
        User user = new User();
        user.setEmail("");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }
}