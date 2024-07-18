package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.util.MinimumDate;

import java.time.LocalDate;

/**
 * Film.
 */
@Builder
@Data
public class Film {
    private int id;
    @NotBlank
    @NotNull
    @NotEmpty
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    @MinimumDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
