package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.util.MinimumDate;

import java.time.LocalDate;

@Builder
@Data
public class Film {
    private int id;
    @NotBlank
    @NotEmpty
    private String name;
    @Size(max = 200)
    private String description;
    @MinimumDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
