package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    public Collection<Film> getPopularFilms(Long count) {
        // Получаем список популярных фильмов
        final Collection<Film> films = filmStorage.getPopularFilms().stream()
                .limit(count)
                .toList();

        // Собираем все id фильмов для запроса жанров и MPA одним запросом
        List<Long> filmIds = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());

        // Получаем жанры для всех фильмов за один запрос
        Map<Long, List<Genre>> genresByFilmId = genreDbStorage.getGenresByFilmIds(filmIds);

        // Получаем MPA для всех фильмов за один запрос
        Map<Long, Mpa> mpaByFilmId = mpaDbStorage.getMpaByFilmIds(filmIds);

        // Привязываем жанры и MPA к фильмам
        for (Film film : films) {
            film.getGenres().addAll(genresByFilmId.getOrDefault(film.getId(), new ArrayList<>()));
            film.setMpa(mpaByFilmId.get(film.getId()));
        }

        return films;
    }

    public Collection<Film> getAllFilms() {
        final Collection<Film> films = filmStorage.getAllFilms();
        if (films.isEmpty()) {
            return films;
        }

        List<Long> filmIds = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());

        // Получаем жанры и MPA для всех фильмов за один запрос
        Map<Long, List<Genre>> genresByFilmId = genreDbStorage.getGenresByFilmIds(filmIds);
        Map<Long, Mpa> mpaByFilmId = mpaDbStorage.getMpaByFilmIds(filmIds);

        // Привязываем жанры и MPA к фильмам
        for (Film film : films) {
            film.getGenres().addAll(genresByFilmId.getOrDefault(film.getId(), Collections.emptyList()));
            film.setMpa(mpaByFilmId.get(film.getId()));
        }

        return films;
    }

    public Film create(Film film) {
        checkMpaAndGenres(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        checkFilmId(film);
        checkMpaAndGenres(film);
        return filmStorage.update(film);
    }

    public Film getFilmById(Long filmId) {
        log.debug("Начата проверка наличия фильма c id = {} в методе FilmById", filmId);
        final Film film = checkFilm(filmId);
        log.debug("Начата проверка наличия Mpa с id = {} в методе FilmById", film.getMpa().getId());
        final Mpa mpa = checkMpa(film.getMpa().getId());
        final Collection<Genre> genres = genreDbStorage.getGenresByFilmId(filmId);
        final Collection<User> users = userStorage.getUsersByFilmId(filmId);
        film.setMpa(mpa);
        film.getGenres().addAll(genres);
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        log.debug("Начата проверка наличия фильма c id = {} и пользователя с id = {} в методе addLike",
                filmId,
                userId);
        checkFilm(filmId);
        checkUser(userId);
        filmStorage.createLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        log.debug("Начата проверка наличия фильма c id = {} и пользователя с id = {} в методе deleteLike",
                filmId,
                userId);
        checkFilm(filmId);
        checkUser(userId);
        filmStorage.deleteLike(filmId, userId);
    }

    private Mpa checkMpa(Integer mpaId) {
        return mpaDbStorage.getMpaById(mpaId)
                .orElseThrow(() -> {
                    log.warn("Mpa c id = {} не найден", mpaId);
                    return new NotFoundException(String.format("Mpa с id = %d не найден", mpaId));
                });
    }

    private void checkMpaAndGenres(Film film) {
        // Получаем все MPA за один запрос
        List<Mpa> allMpas = (List<Mpa>) mpaDbStorage.getAllMpa();
        Map<Integer, Mpa> mpaMap = allMpas.stream()
                .collect(Collectors.toMap(Mpa::getId, mpa -> mpa));

        // Проверяем корректность MPA
        Integer mpaId = film.getMpa().getId();
        if (!mpaMap.containsKey(mpaId)) {
            log.warn("Некорректный у Mpa id = {}", mpaId);
            throw new ValidationException(String.format("Некорректный у Mpa id = %d", mpaId));
        }

        // Получаем все жанры за один запрос
        List<Genre> allGenres = (List<Genre>) genreDbStorage.getAllGenres();
        Map<Integer, Genre> genreMap = allGenres.stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre));

        // Проверяем корректность жанров
        for (Genre genre : film.getGenres()) {
            if (!genreMap.containsKey(genre.getId())) {
                log.warn("Некорректный у Genre id = {}", genre.getId());
                throw new ValidationException(String.format("Некорректный у Genre id = %d", genre.getId()));
            }
        }
    }

    private void checkFilmId(Film film) {
        if (Objects.isNull(film.getId())) {
            log.warn("У фильма {} отсутствует id", film);
            throw new ConditionsNotMetException("id должен быть указан");
        }
    }

    private Film checkFilm(Long filmId) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> {
                    log.warn("Фильм с id = {} не найден", filmId);
                    return new NotFoundException(String.format("Фильм с id = %d не найден", filmId));
                });
    }

    private User checkUser(Long userId) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь c id = {} не найден", userId);
                    return new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
                });
    }
}
