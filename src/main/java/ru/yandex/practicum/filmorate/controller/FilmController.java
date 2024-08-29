package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        final Collection<Film> films = filmService.getAllFilms();
        log.info("Возвращены все фильмы");
        return films;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(value = "id") Long filmId) {
        final Film film = filmService.getFilmById(filmId);
        log.info("Запрос на фильм с id = {}", filmId);
        return film;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable(value = "id") Long id,
                        @PathVariable(value = "userId") Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable(value = "id") Long id,
                           @PathVariable(value = "userId") Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10") Long count) {
        final Collection<Film> popularFilms = filmService.getPopularFilms(count);
        log.info("Запрос на популярные фильмы");
        return popularFilms;
    }
}
