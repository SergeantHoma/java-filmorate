package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import ru.yandex.practicum.filmorate.util.IncludingSpace;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
public class User {
    private Long id;
    @Email
    @NotNull
    private String email;
    @NotBlank
    @IncludingSpace
    private String login;
    private String name;
    @NotNull
    @Past
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();
}
