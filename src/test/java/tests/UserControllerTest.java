package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import controller.UserController;
import ru.yandex.practicum.filmorate.FilmorateApplication;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = FilmorateApplication.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private User user;

    @BeforeEach
    void setup() {
        user = new User(null, "test@example.com", "login", "Name", LocalDate.of(2000, 1, 1));
    }

    @Test
    void addUser_EmptyEmail_BadRequest() throws Exception {
        user.setEmail("  ");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email не может быть пустым")));
    }

    @Test
    void addUser_BirthdayInFuture_BadRequest() throws Exception {
        user.setBirthday(LocalDate.now().plusDays(1));
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Дата рождения не может быть в будущем")));
    }
}