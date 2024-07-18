package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.FilmException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int idFilm = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(getIdForFilm());
        films.put(film.getId(), film);
        log.info("Запрос на создание фильма. Фильм добавлен");
        return film;
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
        if (films.get(film.getId()) != null) {
            films.put(film.getId(), film);
            log.info("Запрос на изменение фильма. Фильм изменён.");
        } else {
            log.warn("Запрос на изменение фильм. Фильм не найден.");
            throw new FilmException("Фильм не найден.");
        }
        return film;
    }

    @GetMapping()
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private int getIdForFilm() {;
        return ++idFilm;
    }
}
