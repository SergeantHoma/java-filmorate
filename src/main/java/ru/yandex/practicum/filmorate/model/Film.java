package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Setter;
import ru.yandex.practicum.filmorate.util.MinimumDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotBlank
    @NotEmpty
    private String name;
    @Size(max = 200)
    private String description;
    @MinimumDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @NotNull
    private Mpa mpa;
    @Setter
    private Set<Genre> genres = new HashSet<>();
}
