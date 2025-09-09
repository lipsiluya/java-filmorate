package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private Long id;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Email должен быть валидным")
    private String email;

    @NotBlank(message = "Логин не должен быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелы")
    private String login;
    private String name;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;

    private Set<Long> friends;
}
