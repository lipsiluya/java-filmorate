package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.converters.*;
import java.time.Duration;
import java.time.LocalDate;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Film.
 */
@Data
public class Film {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза обязательна")
    private LocalDate releaseDate;

    @JsonSerialize(using = DurationSecondsSerializer.class)
    @JsonDeserialize(using = DurationSecondsDeserializer.class)
    private Duration duration;
}
