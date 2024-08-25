package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmException;
import ru.yandex.practicum.filmorate.exeption.UserException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private int idFilm = 0;

    @Override
    public Collection<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long filmId) {
        return Optional.ofNullable(films.get(filmId))
                .orElseThrow(() -> new UserException(String.format("фильм с id: %d не найден", filmId)));
    }

    @Override
    public Film create(Film film) {
        film.setId(getIdForFilm());
        films.put(film.getId(), film);
        log.info("Запрос на создание фильма. Фильм добавлен");
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.get(film.getId()) != null) {
            films.put(film.getId(), film);
            log.info("Запрос на изменение фильма. Фильм изменён.");
        } else {
            log.warn("Запрос на изменение фильм. Фильм не найден.");
            throw new FilmException("Фильм не найден.");
        }
        return film;
    }

    @Override
    public void delete(Long filmId) {
        if (!films.containsKey(filmId)) {
            log.info("Запрос на удаление фильма. Фильм не найден");
        } else {
            films.remove(filmId);
            log.info("Запрос на удаление фильма. Фильм удален");
        }
    }

    private long getIdForFilm() {
        return ++idFilm;
    }
}
