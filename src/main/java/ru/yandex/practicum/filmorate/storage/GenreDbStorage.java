package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {
    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genre> getAllGenres() {
        String findAllQuery = "SELECT * FROM genres";
        return findMany(findAllQuery);
    }

    public Optional<Genre> getGenreById(Integer genreId) {
        String findByGenreIdQuery = "SELECT * FROM genres WHERE id = ?";
        return findOne(findByGenreIdQuery, genreId);
    }

    public Collection<Genre> getGenresByFilmId(Long filmId) {
        String findByFilmIdQuery = "SELECT g.* FROM genres g JOIN film_genre fg " +
                "ON g.id = fg.genre_id WHERE film_id = ? ORDER BY g.id";
        return findMany(findByFilmIdQuery, filmId);
    }

    public Map<Long, List<Genre>> getGenresByFilmIds(List<Long> filmIds) {
        String sql = "SELECT fg.film_id, g.id, g.name FROM film_genre fg " +
                "JOIN genres g ON fg.genre_id = g.id " +
                "WHERE fg.film_id IN (:filmIds)";

        Map<Long, List<Genre>> genresByFilmId = new HashMap<>();

        jdbc.query(sql, (PreparedStatementSetter) Map.of("filmIds", filmIds), rs -> {
            Long filmId = rs.getLong("film_id");

            // Создаем объект Genre и устанавливаем значения через сеттеры
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));

            genresByFilmId.computeIfAbsent(filmId, k -> new ArrayList<>()).add(genre);
        });

        return genresByFilmId;
    }
}
